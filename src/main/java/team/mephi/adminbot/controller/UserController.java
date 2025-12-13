package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.mephi.adminbot.dto.CreateUserFormDto;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.service.PdConsentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PdConsentService pdConsentService;

    @GetMapping("/users")
    public String usersPage(
            @RequestParam(defaultValue = "all") String role,
            @RequestParam(name = "q", required = false) String query,
            Model model) {

        List<User> users;
        boolean searching = query != null && !query.isBlank();

        if (!searching) {
            users = "all".equals(role)
                    ? userRepository.findAll()
                    : userRepository.findByRoleName(role);
        } else {
            users = "all".equals(role)
                    ? userRepository.searchAll(query)
                    : userRepository.searchByRole(role, query);
        }

        //получаем данные по согласию на персональную информацию -> пушим в UI
        Map<Long, PdConsentService.PdConsentView> pdConsents = users.stream()
                        .collect(Collectors.toMap(
                                User::getId, u -> pdConsentService.buildForUserView(u.getId())
                        ));

        List<Role> roles = roleRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("pdConsents", pdConsents);
        model.addAttribute("currentRole", role);

        // для правильной работы поля поиска - значение поиска остается, пользователь стирает значение вручную
        model.addAttribute("search", query == null ? "" : query);
        return "users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute CreateUserFormDto form,
                             RedirectAttributes ra){
        // 1) Роль
        Role role = roleRepository.findByName(form.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + form.getRoleName()));

        // 2) fullName -> firstName/lastName/name
        String fullName = form.getFullName() == null ? "" : form.getFullName().trim();
        String[] parts = fullName.split("\\s+");
        String lastName = parts.length > 0 ? parts[0] : "-";
        String firstName = parts.length > 1 ? parts[1] : "-";
        String name = (lastName + " " + firstName).trim();

        // 3) externalId (временно = telegram)
        String telegram = form.getTelegram() == null ? "" : form.getTelegram().trim();
        if (!telegram.isBlank() && !telegram.startsWith("@")) {
            telegram = "@" + telegram;
        }
        if (telegram.isBlank()) {
            throw new IllegalArgumentException("Telegram is required");
        }

        User user = User.builder()
                .externalId(telegram)     // уникальный ID
                .name(name)
                .firstName(firstName)
                .lastName(lastName)
                .status("active")         // по умолчанию активный
                .role(role)
                .build();

        userRepository.save(user);

        ra.addFlashAttribute("toastTitle", "Пользователь добавлен");
        ra.addFlashAttribute("toastBody",
                "Добавлен пользователь " + name + " с ролью «" + role.getDescription() + "»");
        // редирект обратно на ту вкладку роли, где создавали
        return "redirect:/users?role=" + role.getName();
    }

    // используем CreateUserFormDto, функционал тот же
    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id,
                           @ModelAttribute CreateUserFormDto form,
                           @RequestParam(required = false) String returnUrl,
                           RedirectAttributes ra) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        Role role = roleRepository.findByName(form.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + form.getRoleName()));

        String fullName = form.getFullName() == null ? "" : form.getFullName().trim();
        String[] parts = fullName.split("\\s+");
        String lastName = parts.length > 0 ? parts[0] : "-";
        String firstName = parts.length > 1 ? parts[1] : "-";
        String name = (lastName + " " + firstName).trim();

        String telegram = form.getTelegram() == null ? "" : form.getTelegram().trim();
        if (!telegram.isBlank() && !telegram.startsWith("@")) telegram = "@" + telegram;

        user.setRole(role);
        user.setName(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setExternalId(telegram);

        userRepository.save(user);

        ra.addFlashAttribute("toastTitle", "Пользователь обновлён");
        ra.addFlashAttribute("toastBody", "Обновлён пользователь " + user.getName() + " с ролью «" + role.getDescription() + "»");

        if(returnUrl != null && returnUrl.startsWith("/")){
            return "redirect:" + returnUrl;
        }
        return "redirect:/users?role=" + role.getName();
    }

}