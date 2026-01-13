package team.mephi.adminbot.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик ошибок, связанных с безопасностью.
 * <p>
 * Перехватывает основные ошибки доступа и аутентификации
 * и возвращает понятные JSON-ответы.
 * <p>
 * Здесь обрабатываются:
 * - 401 Unauthorized — пользователь не аутентифицирован
 * - 403 Forbidden — нет прав доступа
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает ошибки аутентификации (401 Unauthorized).
     * <p>
     * Срабатывает, если:
     * - JWT-токен не передан
     * - JWT-токен просрочен или некорректный
     * - аутентификация пользователя не удалась
     */
    @ExceptionHandler({
            AuthenticationException.class,
            AuthenticationCredentialsNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception ex) {

        // Формируем тело ответа с описанием ошибки
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", "Требуется аутентификация. Передайте корректный JWT-токен.");
        body.put("path", "/api");

        // Возвращаем HTTP 401
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    /**
     * Обрабатывает ошибки доступа (403 Forbidden).
     * <p>
     * Срабатывает, если:
     * - пользователь вошёл в систему, но у него нет нужной роли
     * - пользователь пытается получить доступ к ресурсу,
     * на который у него нет прав
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {

        // Формируем тело ответа с описанием ошибки
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", "Доступ запрещён. У вас нет прав на этот ресурс.");
        body.put("path", "/api");

        // Возвращаем HTTP 403
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    /**
     * Обрабатывает все остальные ошибки,
     * которые не были перехвачены выше.
     * <p>
     * Обычно это неожиданные ошибки или баги в коде.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        // Формируем стандартный ответ для ошибки сервера
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());

        // Возвращаем HTTP 500
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
