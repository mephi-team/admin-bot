package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import team.mephi.adminbot.model.Broadcast;
import team.mephi.adminbot.repository.BroadcastRepository;

import java.util.List;

@Controller
public class BroadcastController {

    @Autowired
    private BroadcastRepository broadcastRepository;

    @GetMapping("/broadcasts")
    public String broadcastsPage(Model model) {
        List<Broadcast> broadcasts = broadcastRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("broadcasts", broadcasts);
        model.addAttribute("newBroadcast", Broadcast.builder().build());
        model.addAttribute("currentUri", "broadcasts");
        return "broadcasts";
    }

    @PostMapping("/broadcasts")
    public String createBroadcast(@ModelAttribute("newBroadcast") Broadcast broadcast) {
        // Логика: сохраняем рассылку, бот будет читать её позже
        broadcastRepository.save(broadcast);
        return "redirect:/broadcasts";
    }
}