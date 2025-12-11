package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String usersPage(
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(name = "q", required = false) String query,
            Model model) {

        List<User> users;
        boolean searching = query != null && !query.isBlank();

        if (!searching) {
            // стандартный фильтр без поиска
            if (status.equals("active")) {
                users = userRepository.findByStatus("active");
            } else if (status.equals("blocked")) {
                users = userRepository.findByStatus("blocked");
            } else {
                users = userRepository.findAll();
            }
        } else {
            // поиск с учётом статуса
            if (status.equals("all")) {
                users = userRepository.searchAll(query);
            } else {
                users = userRepository.searchByStatus(status, query);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("currentStatus", status);
        return "users";
    }
}