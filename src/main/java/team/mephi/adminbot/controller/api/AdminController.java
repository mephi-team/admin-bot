package team.mephi.adminbot.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для административных операций.
 *
 * Этот контроллер доступен только администраторам.
 * Для доступа ко всем методам требуется роль ROLE_ADMIN.
 *
 * Проверка роли выполняется через аннотацию:
 * @PreAuthorize("hasRole('ADMIN')")
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    // Репозиторий для работы с пользователями в базе данных
    @Autowired
    private UserRepository userRepository;

    /**
     * GET /api/admin/users
     *
     * Возвращает список всех пользователей.
     * Доступно только администратору.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        // Получаем всех пользователей из базы
        List<User> users = userRepository.findAll();

        // Отдаём список пользователей клиенту
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/admin/users/{id}
     *
     * Возвращает одного пользователя по его ID.
     * Если пользователь не найден — вернётся 404.
     * Доступно только администратору.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                // Если пользователь найден — возвращаем его
                .map(ResponseEntity::ok)
                // Если нет — отдаём 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/admin/users/{id}
     *
     * Удаляет пользователя по ID.
     * Если пользователь существует — удаляем и возвращаем сообщение об успехе.
     * Если нет — возвращаем 404.
     * Доступно только администратору.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // Проверяем, существует ли пользователь с таким ID
        if (userRepository.existsById(id)) {

            // Удаляем пользователя из базы
            userRepository.deleteById(id);

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
     *
     * Возвращает простую статистику для админ-панели:
     * - общее количество пользователей
     * - текущее время на сервере
     *
     * Доступно только администратору.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        // Общее количество пользователей в системе
        stats.put("totalUsers", userRepository.count());

        // Текущее время в миллисекундах (можно использовать для логов или дашборда)
        stats.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(stats);
    }
}

