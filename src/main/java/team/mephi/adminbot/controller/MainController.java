package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        // Общее число пользователей
        long totalUsers = userRepository.count();

        // Активные диалоги (за последние 24 часа)
        long activeDialogs = dialogRepository.countByLastMessageAtAfter(Instant.now().minusSeconds(24 * 3600));

        // Новые вопросы (за последнюю неделю)
        long newQuestions = questionRepository.countByCreatedAtAfter(Instant.now().minusSeconds(7 * 3600));

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeDialogs", activeDialogs);
        model.addAttribute("newQuestions", newQuestions);

        return "main"; // шаблон: src/main/resources/templates/main.html
    }
}