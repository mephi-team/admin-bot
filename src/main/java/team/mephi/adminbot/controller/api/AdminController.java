package team.mephi.adminbot.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для административных операций.
 * <p>
 * Этот контроллер доступен только администраторам.
 * Для доступа ко всем методам требуется роль ROLE_ADMIN.
 * <p>
 * Проверка роли выполняется через аннотацию
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

    // Репозиторий для работы с пользователями в базе данных
    private UserService userService;

    /**
     * GET /api/admin/users
     * <p>
     * Возвращает список всех пользователей.
     * Доступно только администратору.
     */
    @GetMapping("/users")
    public ResponseEntity<List<SimpleUser>> getAllUsers() {
        // Получаем всех пользователей из базы
        List<SimpleUser> users = userService.findAll();

        // Отдаём список пользователей клиенту
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/admin/users/{id}
     * <p>
     * Возвращает одного пользователя по его ID.
     * Если пользователь не найден — вернётся 404.
     * Доступно только администратору.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<SimpleUser> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                // Если пользователь найден — возвращаем его
                .map(ResponseEntity::ok)
                // Если нет — отдаём 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/admin/users/{id}
     * <p>
     * Удаляет пользователя по ID.
     * Если пользователь существует — удаляем и возвращаем сообщение об успехе.
     * Если нет — возвращаем 404.
     * Доступно только администратору.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // Проверяем, существует ли пользователь с таким ID
        if (userService.existsById(id)) {

            // Удаляем пользователя из базы
            userService.deleteAllById(List.of(id));

            // Формируем простой ответ с результатом операции
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("id", id.toString());

            return ResponseEntity.ok(response);
        }

        // Если пользователь не найден — возвращаем 404
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/admin/stats
     * <p>
     * Возвращает простую статистику для админ-панели:
     * - общее количество пользователей
     * - текущее время на сервере
     * <p>
     * Доступно только администратору.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        // Общее количество пользователей в системе
        stats.put("totalUsers", userService.countAllUsers());

        // Текущее время в миллисекундах (можно использовать для логов или дашборда)
        stats.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(stats);
    }
}

