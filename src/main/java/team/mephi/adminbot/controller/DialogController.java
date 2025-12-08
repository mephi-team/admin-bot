package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.repository.DialogRepository;

import java.util.List;

@Controller
public class DialogController {

    @Autowired
    private DialogRepository dialogRepository;

    @GetMapping("/dialogs")
    public String dialogsPage(
            @RequestParam(required = false) String search,
            Model model) {

        List<Dialog> dialogs;
        if (search != null && !search.trim().isEmpty()) {
            dialogs = dialogRepository.searchByUserName(search.trim());
        } else {
            dialogs = dialogRepository.findAllWithUsers();
        }

        model.addAttribute("dialogs", dialogs);
        model.addAttribute("searchQuery", search != null ? search : "");
        return "dialogs";
    }
}