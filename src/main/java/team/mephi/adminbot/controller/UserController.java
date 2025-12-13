package team.mephi.adminbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        //получаем согласие на персональную информацию -> UI
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
}