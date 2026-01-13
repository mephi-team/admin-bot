package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link AuthServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationContext authContext;

    /**
     * Проверяет, что возвращается аутентифицированный пользователь.
     */
    @Test
    void givenAuthenticatedUser_WhenGetUserInfoCalled_ThenUserReturned() {
        // Arrange
        DefaultOidcUser user = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                new OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("email", "user@example.com")),
                "email"
        );
        when(authContext.getAuthenticatedUser(DefaultOidcUser.class)).thenReturn(Optional.of(user));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        DefaultOidcUser result = service.getUserInfo();

        // Assert
        assertThat(result).isEqualTo(user);
    }

    /**
     * Проверяет, что признак администратора определяется по ролям.
     */
    @Test
    void givenAdminRole_WhenIsAdminCalled_ThenReturnsTrue() {
        // Arrange
        when(authContext.getGrantedRoles()).thenReturn(List.of("ADMIN", "USER"));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        boolean isAdmin = service.isAdmin();

        // Assert
        assertThat(isAdmin).isTrue();
    }

    /**
     * Проверяет делегирование выхода в контекст аутентификации.
     */
    @Test
    void givenAuthService_WhenLogoutCalled_ThenDelegatesToContext() {
        // Arrange
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        service.logout();

        // Assert
        verify(authContext).logout();
    }
}
