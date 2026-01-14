package team.mephi.adminbot.vaadin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для VaadinSecurityConfig.
 * Проверяют формирование URL выхода из Keycloak.
 */
@ExtendWith(MockitoExtension.class)
class VaadinSecurityConfigTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    /**
     * Проверяет редирект без id_token_hint при отсутствии аутентификации.
     */
    @Test
    void Given_nullAuthentication_When_onLogoutSuccess_Then_redirectsWithoutIdTokenHint() throws Exception {
        // Arrange
        VaadinSecurityConfig config = new VaadinSecurityConfig();
        LogoutSuccessHandler handler = invokeOidcLogoutSuccessHandler(config);
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        handler.onLogoutSuccess(request, response, null);

        // Assert
        verify(response).sendRedirect(urlCaptor.capture());
        String url = urlCaptor.getValue();
        assertTrue(url.contains("http://localhost:8081/realms/mephi-realm/protocol/openid-connect/logout"));
        assertTrue(url.contains("post_logout_redirect_uri=http://localhost:8080"));
        assertFalse(url.contains("id_token_hint="));
    }

    /**
     * Проверяет редирект с id_token_hint при OIDC-пользователе.
     */
    @Test
    void Given_oidcPrincipal_When_onLogoutSuccess_Then_redirectsWithIdTokenHint() throws Exception {
        // Arrange
        VaadinSecurityConfig config = new VaadinSecurityConfig();
        LogoutSuccessHandler handler = invokeOidcLogoutSuccessHandler(config);

        DefaultOidcUser oidcUser = org.mockito.Mockito.mock(DefaultOidcUser.class);
        OidcIdToken idToken = org.mockito.Mockito.mock(OidcIdToken.class);
        when(idToken.getTokenValue()).thenReturn("abc");
        when(oidcUser.getIdToken()).thenReturn(idToken);
        when(authentication.getPrincipal()).thenReturn(oidcUser);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        handler.onLogoutSuccess(request, response, authentication);

        // Assert
        verify(response).sendRedirect(urlCaptor.capture());
        String url = urlCaptor.getValue();
        assertTrue(url.contains("&id_token_hint=abc"));
    }

    /**
     * Проверяет редирект без id_token_hint при не OIDC-пользователе.
     */
    @Test
    void Given_nonOidcPrincipal_When_onLogoutSuccess_Then_redirectsWithoutIdTokenHint() throws Exception {
        // Arrange
        VaadinSecurityConfig config = new VaadinSecurityConfig();
        LogoutSuccessHandler handler = invokeOidcLogoutSuccessHandler(config);
        when(authentication.getPrincipal()).thenReturn("user");

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        handler.onLogoutSuccess(request, response, authentication);

        // Assert
        verify(response).sendRedirect(urlCaptor.capture());
        String url = urlCaptor.getValue();
        assertFalse(url.contains("id_token_hint="));
    }

    private LogoutSuccessHandler invokeOidcLogoutSuccessHandler(VaadinSecurityConfig config) throws Exception {
        Method method = VaadinSecurityConfig.class.getDeclaredMethod("oidcLogoutSuccessHandler");
        method.setAccessible(true);
        return (LogoutSuccessHandler) method.invoke(config);
    }
}
