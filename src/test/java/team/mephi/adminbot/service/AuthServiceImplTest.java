package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для AuthServiceImpl.
 * Покрывают: получение пользователя, роль и выход.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationContext authContext;
    @Mock
    private DefaultOidcUser oidcUser;

    /**
     * Проверяет получение данных пользователя из контекста.
     */
    @Test
    void Given_authenticatedUser_When_getUserInfo_Then_returnsUser() {
        // Arrange
        when(authContext.getAuthenticatedUser(eq(DefaultOidcUser.class))).thenReturn(Optional.of(oidcUser));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        DefaultOidcUser result = service.getUserInfo();

        // Assert
        assertEquals(oidcUser, result);
    }

    /**
     * Проверяет проверку роли администратора.
     */
    @Test
    void Given_adminRole_When_isAdmin_Then_returnsTrue() {
        // Arrange
        when(authContext.getGrantedRoles()).thenReturn(List.of("ADMIN"));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        boolean result = service.isAdmin();

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет вызов выхода из системы.
     */
    @Test
    void Given_service_When_logout_Then_callsContext() {
        // Arrange
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        // Act
        service.logout();

        // Assert
        verify(authContext).logout();
    }
}
