package team.mephi.adminbot.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Тестовая security-конфигурация, которая:
 * 1) Полностью "перехватывает" /api/** (чтобы Vaadin security туда не лез)
 * 2) Для /api/** даёт:
 * - без аутентификации -> 401 (без редиректов)
 * - с аутентификацией -> роли проверяются через @PreAuthorize
 * 3) Для всего остального -> permitAll (чтобы не триггерить VaadinContext Lookup)
 */
@TestConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityOverrideConfig {

    /**
     * ВАЖНО:
     * /api/** должен быть самым приоритетным chain,
     * иначе запросы будут попадать в VaadinSecurityConfigurer.vaadin()
     * и падать на VaadinContext.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/check").permitAll()
                        .anyRequest().authenticated()
                )
                // 401 вместо редиректов на oauth2/логин
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // чтобы "без логина" было 401
                .httpBasic(Customizer.withDefaults())
                // отключаем формы/редиректы/oidc флоу в тестах
                .formLogin(form -> form.disable())
                .oauth2Login(oauth2 -> oauth2.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }

    /**
     * Всё остальное (включая Vaadin/Hilla пути) просто разрешаем,
     * чтобы Vaadin security не пыталась лезть в VaadinContext в тестах.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain permitAllOtherRequests(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .requestCache(cache -> cache.disable())
                .securityContext(sc -> sc.disable());

        return http.build();
    }
}
