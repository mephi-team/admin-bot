package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для AuthServiceImpl.
 * Проверяют доступ к пользователю, ролям и выходу.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationContext authContext;

    @Mock
    private DefaultOidcUser oidcUser;

    /**
     * Проверяет возврат найденного OIDC-пользователя.
     */
    @Test
    void Given_authenticatedOidcUserPresent_When_getUserInfo_Then_returnsUser() {
        // Arrange
        when(authContext.getAuthenticatedUser(eq(DefaultOidcUser.class))).thenReturn(Optional.of(oidcUser));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        DefaultOidcUser result = service.getUserInfo();

        // Assert
        assertSame(oidcUser, result);
    }

    /**
     * Проверяет выброс исключения при отсутствии OIDC-пользователя.
     */
    @Test
    void Given_authenticatedOidcUserMissing_When_getUserInfo_Then_throwsException() {
        // Arrange
        when(authContext.getAuthenticatedUser(eq(DefaultOidcUser.class))).thenReturn(Optional.empty());
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        Runnable call = service::getUserInfo;

        // Assert
        assertThrows(NoSuchElementException.class, call::run);
    }

    /**
     * Проверяет возврат true при наличии роли ADMIN.
     */
    @Test
    void Given_grantedRolesContainAdmin_When_isAdmin_Then_returnsTrue() {
        // Arrange
        Collection<String> roles = List.of("ADMIN");
        when(authContext.getGrantedRoles()).thenReturn(roles);
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        boolean result = service.isAdmin();

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет возврат false при отсутствии роли ADMIN.
     */
    @Test
    void Given_grantedRolesDoNotContainAdmin_When_isAdmin_Then_returnsFalse() {
        // Arrange
        Collection<String> roles = List.of("ROLE_ADMIN", "admin");
        when(authContext.getGrantedRoles()).thenReturn(roles);
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        boolean result = service.isAdmin();

        // Assert
        assertFalse(result);
    }

    /**
     * Проверяет делегирование выхода в контекст аутентификации.
     */
    @Test
    void Given_anyContext_When_logout_Then_delegatesToAuthContextLogout() {
        // Arrange
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        service.logout();

        // Assert
        verify(authContext).logout();
    }
}
