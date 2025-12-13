package team.mephi.adminbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Основная конфигурация Spring Security для работы с Keycloak и JWT.
 *
 * Что здесь настраивается:
 * - аутентификация по JWT (без сессий)
 * - доступ к эндпоинтам по ролям
 * - CORS для фронтенда
 * - кастомная логика извлечения ролей из токена Keycloak
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // ID клиента в Keycloak, нужен для извлечения клиентских ролей
    @Value("${keycloak.client-id}")
    private String clientId;

    /**
     * Основная цепочка фильтров безопасности.
     *
     * Здесь описываются все правила:
     * кто, куда и с какими правами может обращаться.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, так как используем JWT и не храним сессии
                .csrf(csrf -> csrf.disable())

                // Включаем CORS и указываем свою конфигурацию
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Говорим Spring Security не создавать HTTP-сессии
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Настраиваем правила доступа к эндпоинтам
                .authorizeHttpRequests(auth -> auth

                        // Публичные эндпоинты для мониторинга
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()

                        // Административное API — только для ROLE_ADMIN
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // API эксперта — только для ROLE_LC_EXPERT
                        .requestMatchers("/api/expert/**")
                        .hasRole("LC_EXPERT")

                        // API пользователя — любой аутентифицированный пользователь
                        .requestMatchers("/api/user/**")
                        .authenticated()

                        // Веб-страницы (Thymeleaf) — тоже требуют входа
                        .requestMatchers(
                                "/", "/users/**", "/questions/**",
                                "/analytics/**", "/dialogs/**", "/broadcasts/**"
                        ).permitAll()

                        // Все остальные запросы тоже требуют аутентификации
                        .anyRequest()
                        .authenticated()
                )

                // Настраиваем Resource Server для работы с JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // Используем свой конвертер ролей из JWT
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Создаёт кастомный конвертер JWT → Authentication.
     *
     * Он нужен для того, чтобы корректно вытаскивать роли
     * из токена Keycloak (realm + client roles).
     */
    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter(clientId);
    }

    /**
     * Настройка CORS.
     *
     * Здесь указываются:
     * - откуда можно делать запросы
     * - какие HTTP-методы разрешены
     * - какие заголовки можно передавать
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Разрешённые источники запросов (фронтенд)
        // Для продакшена список лучше ограничить
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:5173"
        ));

        // Разрешённые HTTP-методы
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Разрешённые заголовки (включая Authorization)
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        // Разрешаем передавать куки и заголовки авторизации
        configuration.setAllowCredentials(true);

        // Кэшируем preflight-запросы на 1 час
        configuration.setMaxAge(3600L);

        // Применяем эту CORS-конфигурацию ко всем эндпоинтам
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

