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
 * Здесь настраивается:
 * - аутентификация по JWT (без сессий)
 * - доступ к эндпоинтам в зависимости от ролей
 * - CORS для фронтенда
 * - кастомное извлечение ролей из токена Keycloak
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * ID клиента в Keycloak.
     *
     * Нужен, чтобы доставать клиентские роли
     * из JWT-токена (resource_access).
     */
    @Value("${keycloak.client-id}")
    private String clientId;

    /**
     * Основная цепочка фильтров безопасности.
     *
     * Здесь описано:
     * кто может обращаться к каким эндпоинтам
     * и при каких условиях.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF отключаем, потому что используем JWT
                // и не работаем с HTTP-сессиями
                .csrf(csrf -> csrf.disable())

                // Включаем CORS с нашей конфигурацией
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Запрещаем создание сессий — приложение полностью stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Правила доступа к эндпоинтам
                .authorizeHttpRequests(auth -> auth

                        // Публичные эндпоинты для мониторинга
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()

                        // Админское API — только для ROLE_ADMIN
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // API эксперта — только для ROLE_LC_EXPERT
                        .requestMatchers("/api/expert/**")
                        .hasRole("LC_EXPERT")

                        // API пользователя — любой залогиненный пользователь
                        .requestMatchers("/api/user/**")
                        .authenticated()

                        // Веб-страницы (Thymeleaf) — тоже только после входа
                        .requestMatchers(
                                "/", "/users", "/questions",
                                "/analytics", "/dialogs", "/broadcasts"
                        ).authenticated()

                        // Всё остальное — тоже требует аутентификации
                        .anyRequest()
                        .authenticated()
                )

                // Настройка Resource Server для проверки JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // Используем свой конвертер ролей
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Кастомный конвертер JWT → Authentication.
     *
     * Используется для корректного извлечения ролей
     * из токена Keycloak (realm roles + client roles).
     */
    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter(clientId);
    }

    /**
     * Настройка CORS.
     *
     * Здесь указываем:
     * - какие источники могут делать запросы
     * - какие методы разрешены
     * - какие заголовки можно передавать
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Разрешённые источники (фронтенд)
        // В продакшене список лучше сузить
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:5173"
        ));

        // Разрешённые HTTP-методы
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Разрешённые заголовки, включая Authorization
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        // Разрешаем передавать cookies и заголовки авторизации
        configuration.setAllowCredentials(true);

        // Кэшируем preflight-запросы на 1 час
        configuration.setMaxAge(3600L);

        // Применяем CORS-настройки ко всем эндпоинтам
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

