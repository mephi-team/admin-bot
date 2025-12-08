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
            Model model) {

        List<User> users;
        if ("active".equals(status)) {
            users = userRepository.findByStatus("active");
        } else if ("blocked".equals(status)) {
            users = userRepository.findByStatus("blocked");
        } else {
            users = userRepository.findAll();
        }

        model.addAttribute("users", users);
        model.addAttribute("currentStatus", status);
        return "users";
    }
}