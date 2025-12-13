package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Страница со списком пользователей.
     *
     * Поддерживает:
     * - фильтрацию по статусу (active / blocked / all)
     * - поиск по строке (имя, телефон, email и т.д.)
     */
    @GetMapping("/users")
    public String usersPage(
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(name = "q", required = false) String query,
            Model model) {

        List<User> users;

        // Проверяем, используется ли поиск
        boolean searching = query != null && !query.isBlank();

        if (!searching) {
            // Обычный список пользователей, без строки поиска
            if (status.equals("active")) {
                users = userRepository.findByStatus(UserStatus.ACTIVE);
            } else if (status.equals("blocked")) {
                users = userRepository.findByStatus(UserStatus.BLOCKED);
            } else {
                // status = all
                users = userRepository.findAll();
            }
        } else {
            // Поиск пользователей с учётом выбранного статуса
            if (status.equals("all")) {
                users = userRepository.searchAll(query);
            } else {
                try {
                    // Пробуем преобразовать статус из строки в enum
                    UserStatus userStatus =
                            UserStatus.valueOf(status.toUpperCase());

                    users = userRepository.searchByStatus(userStatus, query);
                } catch (IllegalArgumentException e) {
                    // Если статус некорректный — ищем без фильтра по статусу
                    users = userRepository.searchAll(query);
                }
            }
        }

        // Передаём данные в шаблон
        model.addAttribute("users", users);
        model.addAttribute("currentStatus", status);

        // Чтобы строка поиска не очищалась автоматически
        model.addAttribute("search", query == null ? "" : query);

        return "users";
    }
}
