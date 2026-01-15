package team.mephi.adminbot.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import team.mephi.adminbot.config.TestSecurityConfig;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
@Import(TestSecurityConfig.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    // ===== /api/user/profile =====

    private static Authentication jwtAuth(String sub, Map<String, Object> claims, String... authorities) {
        Jwt.Builder builder = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .subject(sub);

        claims.forEach(builder::claim);

        Jwt jwt = builder.build();

        List<SimpleGrantedAuthority> auths = List.of(authorities).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // principal = Jwt (важно для UserApiController)
        return new UsernamePasswordAuthenticationToken(jwt, "n/a", auths);
    }

    @Test
    void Given_validJwtAndUserExistsInDb_When_getProfile_Then_returnsJwtFieldsPlusDbFields() throws Exception {
        // Arrange
        String email = "user-id@example.com";
        User user = User.builder()
                .id(5L)
                .userName("User Name")
                .email(email)
                .firstName("User")
                .lastName("Name")
                .status(UserStatus.ACTIVE)
                .role(Role.builder().code("STUDENT").name("STUDENT").description("STUDENT").build())
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Authentication auth = jwtAuth(
                "kc-sub-123",
                Map.of(
                        "email", email,
                        "name", "John Doe",
                        "preferred_username", "john"
                ),
                "ROLE_USER"
        );

        // Act + Assert
        mockMvc.perform(get("/api/user/profile")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keycloakUserId").value("kc-sub-123"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.preferredUsername").value("john"))
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.userName").value("User Name"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void Given_validJwtButEmailMissing_When_getProfile_Then_returnsOnlyJwtFieldsAndDoesNotQueryDb() throws Exception {
        // Arrange
        Authentication auth = jwtAuth(
                "kc-sub-456",
                Map.of(
                        "name", "No Email",
                        "preferred_username", "noemail"
                ),
                "ROLE_USER"
        );

        // Act + Assert
        mockMvc.perform(get("/api/user/profile")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keycloakUserId").value("kc-sub-456"))
                .andExpect(jsonPath("$.email").doesNotExist()) // claim отсутствует => null, Jackson обычно не сериализует null
                .andExpect(jsonPath("$.name").value("No Email"))
                .andExpect(jsonPath("$.preferredUsername").value("noemail"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.userName").doesNotExist())
                .andExpect(jsonPath("$.status").doesNotExist());

        verifyNoInteractions(userRepository);
    }

    @Test
    void Given_validJwtAndUserMissingInDb_When_getProfile_Then_returnsJwtFieldsOnly() throws Exception {
        // Arrange
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Authentication auth = jwtAuth(
                "kc-sub-789",
                Map.of(
                        "email", email,
                        "name", "Missing User",
                        "preferred_username", "missing"
                ),
                "ROLE_USER"
        );

        // Act + Assert
        mockMvc.perform(get("/api/user/profile")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keycloakUserId").value("kc-sub-789"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Missing User"))
                .andExpect(jsonPath("$.preferredUsername").value("missing"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.userName").doesNotExist())
                .andExpect(jsonPath("$.status").doesNotExist());

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    // ===== /api/user/me =====

    @Test
    void Given_noAuthentication_When_getProfile_Then_returns401() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userRepository);
    }

    @Test
    void Given_validJwt_When_getMe_Then_returnsBasicJwtInfoAndAuthorities() throws Exception {
        // Arrange
        Authentication auth = jwtAuth(
                "kc-sub-me-1",
                Map.of(
                        "email", "me@example.com",
                        "name", "Me",
                        "preferred_username", "me"
                ),
                "ROLE_USER", "ROLE_STUDENT"
        );

        // Act + Assert
        mockMvc.perform(get("/api/user/me")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sub").value("kc-sub-me-1"))
                .andExpect(jsonPath("$.email").value("me@example.com"))
                .andExpect(jsonPath("$.name").value("Me"))
                .andExpect(jsonPath("$.preferredUsername").value("me"))
                .andExpect(jsonPath("$.authorities").isArray())
                .andExpect(jsonPath("$.authorities[0]").value("ROLE_USER"));

        verifyNoInteractions(userRepository);
    }

    // ===== /api/user/check =====

    @Test
    void Given_noAuthentication_When_getMe_Then_returns401() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userRepository);
    }

    // ===== helpers =====

    @Test
    void Given_noAuthentication_When_check_Then_returnsOkBecausePermitted() throws Exception {
        mockMvc.perform(get("/api/user/check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("authenticated"))
                .andExpect(jsonPath("$.message").value("User is authenticated"));

        verifyNoInteractions(userRepository);
    }
}
