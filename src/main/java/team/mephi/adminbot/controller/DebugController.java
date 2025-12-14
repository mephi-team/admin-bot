package team.mephi.adminbot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class DebugController {

    @GetMapping("/debug-roles")
    public String debugRoles(Authentication authentication, Model model) {
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Вывод ролей в консоль для отладки
            System.out.println("--- User Authorities Debug ---");
            authorities.forEach(authority -> System.out.println("Role: " + authority.getAuthority()));
            System.out.println("------------------------------");

            // Передача ролей в Thymeleaf модель
            model.addAttribute("roles", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        } else {
            model.addAttribute("roles", java.util.Collections.singletonList("Anonymous"));
        }
        return "debug-template"; // Создайте этот шаблон
    }
}
