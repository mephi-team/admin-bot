package team.mephi.adminbot.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST-контроллер для работы с текущим пользователем.
 * <p>
 * Доступен любому аутентифицированному пользователю.
 * Здесь пользователь может получить информацию о себе
 * на основе JWT-токена.
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    // Репозиторий для работы с пользователями в базе данных
    @Autowired
    private UserRepository userRepository;

    /**
     * GET /api/user/profile
     * <p>
     * Возвращает расширенную информацию о текущем пользователе.
     * <p>
     * Данные берутся из JWT-токена, который пришёл от Keycloak.
     * ID пользователя достаётся из поля "sub".
     * <p>
     * Если пользователь есть в нашей базе,
     * дополнительно возвращаются данные из БД.
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile(Authentication authentication) {

        // Если пользователь не аутентифицирован
        // или principal не является JWT — возвращаем 401
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(401).build();
        }

        // Достаём JWT-токен из контекста безопасности
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // subject — это ID пользователя в Keycloak
        String subject = jwt.getSubject();

        // Получаем email из JWT для поиска пользователя
        String email = jwt.getClaimAsString("email");

        // Пытаемся найти пользователя в базе по email
        // (используем email из JWT, так как это надежный идентификатор)
        Optional<User> userOpt = email != null
                ? userRepository.findByEmail(email)
                : Optional.empty();

        // Формируем объект профиля, который вернём клиенту
        Map<String, Object> profile = new HashMap<>();

        // Данные из Keycloak / JWT
        profile.put("keycloakUserId", subject);
        profile.put("email", jwt.getClaimAsString("email"));
        profile.put("name", jwt.getClaimAsString("name"));
        profile.put("preferredUsername", jwt.getClaimAsString("preferred_username"));
        profile.put("roles", authentication.getAuthorities());

        // Если пользователь найден в базе —
        // добавляем данные из нашей БД
        userOpt.ifPresent(user -> {
            profile.put("userId", user.getId());
            profile.put("userName", user.getUserName());  // Use fullName instead of deprecated getName()
            profile.put("status", user.getStatus());
        });

        return ResponseEntity.ok(profile);
    }

    /**
     * GET /api/user/me
     * <p>
     * Возвращает базовую информацию о текущем пользователе.
     * Удобно использовать для проверки,
     * кто сейчас залогинен в системе.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {

        // Проверяем, что пользователь действительно аутентифицирован
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем JWT-токен
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Формируем простой объект с данными пользователя
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("email", jwt.getClaimAsString("email"));
        userInfo.put("name", jwt.getClaimAsString("name"));
        userInfo.put("preferredUsername", jwt.getClaimAsString("preferred_username"));

        // Список ролей и прав пользователя
        userInfo.put("authorities", authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList());

        return ResponseEntity.ok(userInfo);
    }

    /**
     * GET /api/user/check
     * <p>
     * Простой эндпоинт для проверки,
     * что пользователь успешно аутентифицирован.
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkAuthentication() {

        // Формируем простой ответ "всё ок"
        Map<String, String> response = new HashMap<>();
        response.put("status", "authenticated");
        response.put("message", "User is authenticated");

        return ResponseEntity.ok(response);
    }
}
