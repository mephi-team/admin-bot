package team.mephi.adminbot.controller.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.config.TestSecurityConfig;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API-тесты для AdminController.
 * Проверяют доступ по ролям (ROLE_ADMIN), статус-коды и базовые ответы контроллера.
 */
@WebMvcTest(controllers = AdminController.class)
@Import(TestSecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    /**
     * Набор эндпоинтов AdminController для параметризованных проверок security (401/403).
     */
    private static Stream<Arguments> adminEndpoints() {
        return Stream.of(
                Arguments.of(HttpMethod.GET, "/api/admin/users"),
                Arguments.of(HttpMethod.GET, "/api/admin/users/1"),
                Arguments.of(HttpMethod.DELETE, "/api/admin/users/2"),
                Arguments.of(HttpMethod.GET, "/api/admin/stats")
        );
    }

    /**
     * Проверяет: ROLE_ADMIN может получить список пользователей (200 OK).
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_adminRole_When_getAllUsers_Then_returnsOkAndList() throws Exception {
        // Arrange
        User u1 = new User();
        u1.setId(10L);
        u1.setUserName("Admin List User");
        u1.setEmail("admin-list-user@example.com");

        when(userRepository.findAll()).thenReturn(List.of(u1));

        // Act + Assert
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].userName").value("Admin List User"));

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: при существующем пользователе возвращается 200 OK.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_existingUser_When_getUserById_Then_returnsOk() throws Exception {
        // Arrange
        long id = 5L;
        User user = new User();
        user.setId(id);
        user.setUserName("User Name");
        user.setEmail("user-id@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act + Assert
        mockMvc.perform(get("/api/admin/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.userName").value("User Name"));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: при отсутствии пользователя возвращается 404 Not Found.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_missingUser_When_getUserById_Then_returnsNotFound() throws Exception {
        long id = 999_999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/users/{id}", id))
                .andExpect(status().isNotFound());

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: при существующем пользователе удаление возвращает 200 OK и сообщение.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_existingUser_When_deleteUser_Then_returnsOkAndMessage() throws Exception {
        long id = 7L;
        when(userRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/admin/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.id").value("7"));

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: при отсутствии пользователя удаление возвращает 404 Not Found.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_missingUser_When_deleteUser_Then_returnsNotFound() throws Exception {
        long id = 888_888L;
        when(userRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/admin/users/{id}", id))
                .andExpect(status().isNotFound());

        verify(userRepository).existsById(id);
        verify(userRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: статистика администратора возвращает 200 OK и нужные поля.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void Given_adminRole_When_getAdminStats_Then_returnsOkAndFields() throws Exception {
        when(userRepository.count()).thenReturn(42L);

        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(42))
                .andExpect(jsonPath("$.timestamp").isNumber());

        verify(userRepository).count();
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Проверяет: пользователь с ролью USER получает 403 Forbidden на админских эндпоинтах.
     */
    @ParameterizedTest
    @MethodSource("adminEndpoints")
    @WithMockUser(roles = "USER")
    void Given_userRole_When_requestingAdminEndpoints_Then_returnsForbidden(HttpMethod method, String uri) throws Exception {
        mockMvc.perform(request(method, uri))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userRepository);
    }

    /**
     * Проверяет: без аутентификации возвращается 401 Unauthorized на админских эндпоинтах.
     */
    @ParameterizedTest
    @MethodSource("adminEndpoints")
    void Given_noAuthentication_When_requestingAdminEndpoints_Then_returnsUnauthorized(HttpMethod method, String uri) throws Exception {
        mockMvc.perform(request(method, uri))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userRepository);
    }
}
