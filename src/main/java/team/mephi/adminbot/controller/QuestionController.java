package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.QuestionRepository;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    // Отображение страницы с вопросами
    @GetMapping("/questions")
    public String questionsPage(Model model) {
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        model.addAttribute("newQuestion", new Question());
        return "questions";
    }

    // Обработка добавления нового вопроса
    @PostMapping("/questions")
    public String addQuestion(@ModelAttribute("newQuestion") Question question) {
        questionRepository.save(question);
        return "redirect:/questions";
    }
}
