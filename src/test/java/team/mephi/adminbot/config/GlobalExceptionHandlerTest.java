package team.mephi.adminbot.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Юнит-тесты для GlobalExceptionHandler.
 * Проверяют формирование ответов при ошибках безопасности.
 */
class GlobalExceptionHandlerTest {

    /**
     * Проверяет возврат 401 при ошибках аутентификации.
     */
    @Test
    void Given_authenticationException_When_handleAuthenticationException_Then_returnsUnauthorized() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        AuthenticationCredentialsNotFoundException exception =
                new AuthenticationCredentialsNotFoundException("no token");

        // Act
        ResponseEntity<Map<String, Object>> response = handler.handleAuthenticationException();

        // Assert
        assertEquals(401, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("Требуется аутентификация. Передайте корректный JWT-токен.", body.get("message"));
    }

    /**
     * Проверяет возврат 403 при отсутствии прав доступа.
     */
    @Test
    void Given_accessDeniedException_When_handleAccessDeniedException_Then_returnsForbidden() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        AccessDeniedException exception = new AccessDeniedException("denied");

        // Act
        ResponseEntity<Map<String, Object>> response = handler.handleAccessDeniedException();

        // Assert
        assertEquals(403, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(403, body.get("status"));
        assertEquals("Forbidden", body.get("error"));
        assertEquals("Доступ запрещён. У вас нет прав на этот ресурс.", body.get("message"));
    }
}
