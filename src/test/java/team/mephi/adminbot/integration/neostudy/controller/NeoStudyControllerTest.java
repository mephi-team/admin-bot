package team.mephi.adminbot.integration.neostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import team.mephi.adminbot.config.TestSecurityOverrideConfig;
import team.mephi.adminbot.integration.IntegrationTestBase;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyWebhookPayload;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.DirectionRepository;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные API-тесты для NeoStudyController.
 * <p>
 * Цели:
 * - Security: 401 без редиректов, 403 для ROLE_USER, 200 для ROLE_ADMIN
 * - Бизнес-ветки контроллера/сервиса:
 * register/sync/syncCourses/enrollments/webhooks/health
 * <p>
 * В тестах используем oidcLogin() (Servlet), как в вашей модели oauth2Login/OIDC.
 */
@Import(TestSecurityOverrideConfig.class)
class NeoStudyControllerTest extends IntegrationTestBase {

    private static final AtomicReference<FailureMode> FAILURE_MODE =
            new AtomicReference<>(FailureMode.NONE);

    private static HttpServer mockServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @DynamicPropertySource
    static void neoStudyProperties(DynamicPropertyRegistry registry) {
        startMockServer();

        registry.add("neostudy.base-url", () -> "http://localhost:" + mockServer.getAddress().getPort());
        registry.add("neostudy.enabled", () -> true);

        // Минимизируем таймауты/ретраи, чтобы тесты не тормозили
        registry.add("neostudy.retry.max-attempts", () -> 1);
        registry.add("neostudy.retry.backoff-delay", () -> "1ms");
        registry.add("neostudy.retry.multiplier", () -> 1.0);

        registry.add("neostudy.timeout.connect", () -> "1s");
        registry.add("neostudy.timeout.read", () -> "1s");
        registry.add("neostudy.timeout.write", () -> "1s");
    }

    @AfterAll
    static void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop(0);
        }
    }

    // =========================
    // Security: 401 / 403
    // =========================

    private static Stream<Arguments> neostudyEndpoints() {
        return Stream.of(
                Arguments.of(HttpMethod.POST, "/api/neostudy/users/1/register", null),
                Arguments.of(HttpMethod.POST, "/api/neostudy/users/2/sync", null),
                Arguments.of(HttpMethod.POST, "/api/neostudy/courses/sync", null),
                Arguments.of(HttpMethod.GET, "/api/neostudy/health", null),

                // эндпоинты с body — чтобы security проверялась корректно (не падало на биндинге)
                Arguments.of(HttpMethod.POST, "/api/neostudy/enrollments",
                        """
                                {"userId":1,"directionId":2,"status":"active"}
                                """),
                Arguments.of(HttpMethod.POST, "/api/neostudy/webhooks",
                        """
                                {"eventType":"enrollment.updated","timestamp":"2024-01-10T12:00:00","data":{"status":"active"}}
                                """)
        );
    }

    private static MockHttpServletRequestBuilder withBodyIfNeeded(MockHttpServletRequestBuilder builder, String body) {
        if (body == null) return builder;
        return builder.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    // =========================
    // register user
    // =========================

    private static void startMockServer() {
        if (mockServer != null) return;

        try {
            mockServer = HttpServer.create(new InetSocketAddress(0), 0);
            mockServer.createContext("/api/v1/users", NeoStudyControllerTest::handleUsers);
            mockServer.createContext("/api/v1/courses", NeoStudyControllerTest::handleCourses);
            mockServer.createContext("/api/v1/enrollments", NeoStudyControllerTest::handleEnrollments);
            mockServer.start();
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось запустить mock-сервер NeoStudy", e);
        }
    }

    private static void handleUsers(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method)) {
            sendJson(exchange, 404, "");
            return;
        }

        if ("POST".equals(method) && "/api/v1/users".equals(path)) {
            sendJson(exchange, 200, "{\"id\":\"neo-123\",\"external_id\":\"tg-1\"}");
            return;
        }

        if ("PUT".equals(method) && path.startsWith("/api/v1/users/")) {
            if (FAILURE_MODE.get() == FailureMode.UPDATE_USER) {
                sendJson(exchange, 500, "{\"message\":\"update failed\"}");
                return;
            }
            sendJson(exchange, 200, "{\"id\":\"neo-123\",\"external_id\":\"tg-1\"}");
            return;
        }

        sendJson(exchange, 404, "");
    }

    private static void handleCourses(HttpExchange exchange) throws IOException {
        if (FAILURE_MODE.get() == FailureMode.SYNC_COURSES) {
            sendJson(exchange, 500, "{\"message\":\"courses failed\"}");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 200, "[{\"id\":\"course-1\",\"code\":\"COURSE-1\",\"name\":\"Course\"}]");
            return;
        }

        sendJson(exchange, 404, "");
    }

    // =========================
    // sync user
    // =========================

    private static void handleEnrollments(HttpExchange exchange) throws IOException {
        if (FAILURE_MODE.get() == FailureMode.CREATE_ENROLLMENT) {
            sendJson(exchange, 500, "{\"message\":\"enroll failed\"}");
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 200, "{\"id\":\"enroll-1\",\"status\":\"active\",\"progress\":10}");
            return;
        }

        sendJson(exchange, 404, "");
    }

    private static void sendJson(HttpExchange exchange, int status, String payload) throws IOException {
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    @ParameterizedTest
    @MethodSource("neostudyEndpoints")
    void Given_noAuthentication_When_requestingNeoStudyEndpoints_Then_returnsUnauthorized(
            HttpMethod method,
            String uri,
            String body
    ) throws Exception {
        mockMvc.perform(withBodyIfNeeded(request(method, uri), body))
                .andExpect(status().isUnauthorized());
    }

    // =========================
    // sync courses
    // =========================

    @ParameterizedTest
    @MethodSource("neostudyEndpoints")
    void Given_userRole_When_requestingNeoStudyEndpoints_Then_returnsForbidden(
            HttpMethod method,
            String uri,
            String body
    ) throws Exception {
        mockMvc.perform(withBodyIfNeeded(request(method, uri), body)
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void Given_existingUser_When_registerUser_Then_returnsOk() throws Exception {
        FAILURE_MODE.set(FailureMode.NONE);
        User user = createUser("register-user@example.com");

        mockMvc.perform(post("/api/neostudy/users/{id}/register", user.getId())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.neostudyExternalId").exists())
                .andExpect(jsonPath("$.syncedAt").exists());
    }

    // =========================
    // enrollments
    // =========================

    @Test
    void Given_missingUser_When_registerUser_Then_returnsNotFound() throws Exception {
        long missingId = 123_456L;

        mockMvc.perform(post("/api/neostudy/users/{id}/register", missingId)
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void Given_serviceFailure_When_registerUser_Then_returnsServerError() throws Exception {
        FAILURE_MODE.set(FailureMode.UPDATE_USER);
        User user = createUser("register-fail@example.com");

        mockMvc.perform(post("/api/neostudy/users/{id}/register", user.getId())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void Given_existingUser_When_syncUser_Then_returnsOk() throws Exception {
        FAILURE_MODE.set(FailureMode.NONE);

        User user = createUser("sync-user@example.com");
        user.setNeostudyExternalId("neo-1");
        userRepository.save(user);

        mockMvc.perform(post("/api/neostudy/users/{id}/sync", user.getId())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.neostudyExternalId").exists())
                .andExpect(jsonPath("$.syncedAt").exists());
    }

    // =========================
    // webhooks
    // =========================

    @Test
    void Given_missingUser_When_syncUser_Then_returnsNotFound() throws Exception {
        long missingId = 654_321L;

        mockMvc.perform(post("/api/neostudy/users/{id}/sync", missingId)
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNotFound());
    }

    // =========================
    // health
    // =========================

    @Test
    void Given_serviceFailure_When_syncUser_Then_returnsServerError() throws Exception {
        FAILURE_MODE.set(FailureMode.UPDATE_USER);

        User user = createUser("sync-fail@example.com");
        user.setNeostudyExternalId("neo-2");
        userRepository.save(user);

        mockMvc.perform(post("/api/neostudy/users/{id}/sync", user.getId())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    // =========================
    // helpers
    // =========================

    @Test
    void Given_adminRole_When_syncCourses_Then_returnsOk() throws Exception {
        FAILURE_MODE.set(FailureMode.NONE);

        mockMvc.perform(post("/api/neostudy/courses/sync")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.syncedCount").value(1))
                .andExpect(jsonPath("$.directions[0].code").value("COURSE-1"));
    }

    @Test
    void Given_serviceFailure_When_syncCourses_Then_returnsServerError() throws Exception {
        FAILURE_MODE.set(FailureMode.SYNC_COURSES);

        mockMvc.perform(post("/api/neostudy/courses/sync")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void Given_validEnrollmentRequest_When_createEnrollment_Then_returnsOk() throws Exception {
        FAILURE_MODE.set(FailureMode.NONE);

        User user = createUser("enroll-user@example.com");
        user.setNeostudyExternalId("neo-user");
        userRepository.save(user);

        Direction direction = directionRepository.save(Direction.builder()
                .code("DIR-" + System.nanoTime())
                .name("Направление")
                .neostudyExternalId("neo-course")
                .build());

        Map<String, Object> payload = Map.of(
                "userId", user.getId(),
                "directionId", direction.getId(),
                "status", "active"
        );

        mockMvc.perform(post("/api/neostudy/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.enrollmentId").value("enroll-1"))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.progress").value(10));
    }

    @Test
    void Given_missingUserOrDirection_When_createEnrollment_Then_returnsBadRequest() throws Exception {
        Map<String, Object> payload = Map.of("userId", 12, "directionId", 15);

        mockMvc.perform(post("/api/neostudy/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Пользователь или направление не найдены"));
    }

    // =========================
    // mock NeoStudy HTTP server
    // =========================

    @Test
    void Given_serviceFailure_When_createEnrollment_Then_returnsServerError() throws Exception {
        FAILURE_MODE.set(FailureMode.CREATE_ENROLLMENT);

        User user = createUser("enroll-fail@example.com");
        user.setNeostudyExternalId("neo-user-2");
        userRepository.save(user);

        Direction direction = directionRepository.save(Direction.builder()
                .code("DIR-" + System.nanoTime())
                .name("Направление")
                .neostudyExternalId("neo-course-2")
                .build());

        Map<String, Object> payload = Map.of(
                "userId", user.getId(),
                "directionId", direction.getId(),
                "status", "active"
        );

        mockMvc.perform(post("/api/neostudy/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void Given_validPayload_When_receiveWebhook_Then_returnsOk() throws Exception {
        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType("enrollment.updated")
                .timestamp(LocalDateTime.of(2024, 1, 10, 12, 0))
                .data(Map.of("status", "active"))
                .build();

        mockMvc.perform(post("/api/neostudy/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Вебхук успешно обработан"));
    }

    @Test
    void Given_authenticatedAdmin_When_health_Then_returnsOk() throws Exception {
        mockMvc.perform(get("/api/neostudy/health")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.integration").value("NeoStudy"));
    }

    private User createUser(String email) {
        Role role = getOrCreateRole("STUDENT");
        return userRepository.save(User.builder()
                .userName("Neo User")
                .email(email)
                .firstName("Neo")
                .lastName("Study")
                .role(role)
                .status(UserStatus.ACTIVE)
                .tgId("tg-" + System.nanoTime())
                .build());
    }

    private Role getOrCreateRole(String code) {
        Optional<Role> existing = roleRepository.findByCode(code);
        if (existing.isPresent()) {
            return existing.get();
        }
        return roleRepository.save(Role.builder()
                .code(code)
                .name(code)
                .description(code)
                .build());
    }

    private enum FailureMode {
        NONE,
        UPDATE_USER,
        SYNC_COURSES,
        CREATE_ENROLLMENT
    }
}
