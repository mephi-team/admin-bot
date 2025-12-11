package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.List;

@Controller
public class MailingController {

    @Autowired
    private MailingRepository mailingRepository;

    @GetMapping("/broadcasts")
    public String broadcastsPage(Model model) {
        List<Mailing> broadcasts = mailingRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("broadcasts", broadcasts);
        model.addAttribute("newBroadcast", Mailing.builder().build());
        model.addAttribute("currentUri", "broadcasts");
        return "broadcasts";
    }

    @PostMapping("/broadcasts")
    public String createBroadcast(@ModelAttribute("newBroadcast") Mailing broadcast) {
        // Логика: сохраняем рассылку, бот будет читать её позже
        mailingRepository.save(broadcast);
        return "redirect:/broadcasts";
    }
}