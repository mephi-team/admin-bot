package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import team.mephi.adminbot.dto.MessagesGroup;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Message;
import team.mephi.adminbot.repository.DialogRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        model.addAttribute("today", LocalDate.now());
        return "dialogs/list";
    }

    @GetMapping("/dialogs/{id}")
    public String viewDialog(@PathVariable Long id, @RequestParam(required = false) String search, Model model) {
        List<Dialog> dialogs;
        if (search != null && !search.trim().isEmpty()) {
            dialogs = dialogRepository.searchByUserName(search.trim());
        } else {
            dialogs = dialogRepository.findAllWithUsers();
        }

        model.addAttribute("dialogs", dialogs);

        Dialog dialog = dialogRepository.findById(id).orElseThrow();

        List<MessagesGroup> messageGroups = groupMessagesByDate(dialog.getMessages());

        model.addAttribute("dialog", dialog);
        model.addAttribute("messageGroups", messageGroups);
        model.addAttribute("today", LocalDate.now());
        return "dialogs/detail"; // ← шаблон деталей
    }

    private List<MessagesGroup> groupMessagesByDate(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        // Сортируем по времени (на случай, если порядок нарушен)
        List<Message> sorted = messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());

        // Группируем по дате (LocalDate)
        Map<LocalDate, List<Message>> grouped = sorted.stream()
                .collect(Collectors.groupingBy(
                        msg -> msg.getCreatedAt().toLocalDate(),
                        LinkedHashMap::new, // сохраняем порядок
                        Collectors.toList()
                ));

        // Преобразуем в список MessagesGroup с красивыми метками
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter russianMonthFormatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));

        List<MessagesGroup> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Message>> entry : grouped.entrySet()) {
            LocalDate date = entry.getKey();
            String label;
            if (date.equals(today)) {
                label = "Сегодня";
            } else if (date.equals(yesterday)) {
                label = "Вчера";
            } else {
                label = date.format(russianMonthFormatter);
            }
            result.add(new MessagesGroup(label, entry.getValue()));
        }

        return result;
    }
}