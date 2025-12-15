package team.mephi.adminbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.*;

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

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    /**
     * ID клиента в Keycloak.
     *
     * Нужен, чтобы доставать клиентские роли
     * из JWT-токена (resource_access).
     */
    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    String jwkSetUri;

    @Value("${app.redirect-url}")
    String redirectUrl;

    /**
     * Основная цепочка фильтров безопасности.
     *
     * Здесь описано:
     * кто может обращаться к каким эндпоинтам
     * и при каких условиях.
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // CSRF отключаем, потому что используем JWT
//                // и не работаем с HTTP-сессиями
//                .csrf(csrf -> csrf.disable())
//
//                // Включаем CORS с нашей конфигурацией
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//                // Правила доступа к эндпоинтам
//                .authorizeHttpRequests(auth -> auth
//
//                        // Публичные эндпоинты для мониторинга
//                        .requestMatchers("/actuator/health", "/actuator/info")
//                        .permitAll()
//                        // только для ROLE_LC_EXPERT и ADMIN
//                        .requestMatchers("/questions")
//                        .hasAnyRole("LC_EXPERT", "ADMIN")
//                        // только для ROLE_ADMIN
//                        .requestMatchers("/analytics", "/dialogs", "/broadcasts","/users")
//                        .hasRole("ADMIN")
//                        // Всё остальное — тоже требует аутентификации
//                        .anyRequest()
//                        .authenticated()
//                )
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                // Настраиваем сервис для получения информации о пользователе и ролях
//                                .userInfoEndpoint(userInfo ->
//                                        userInfo.oidcUserService(oidcUserService())
//                                )
//                )
//                // Настройка выхода из системы
//                .logout(logout ->
//                        logout
//                                .logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/logout"))
//                                .logoutSuccessUrl("/")
//                                .logoutSuccessHandler(oidcLogoutSuccessHandler())
//                                .invalidateHttpSession(true)
//                                .deleteCookies("JSESSIONID")
//                );
//
//        return http.build();
//    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            String accessTokenValue = userRequest.getAccessToken().getTokenValue();

            // Создаем декодер для парсинга строки токена в объект JWT
            JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(
                    userRequest.getClientRegistration().getProviderDetails().getJwkSetUri()
            ).build();

            // Парсим токен в объект Spring Security Jwt
            Jwt jwt = decoder.decode(accessTokenValue);
            Map<String, Object> allClaims = jwt.getClaims();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            Map<String, Object> realmAccess = (Map<String, Object>) allClaims.getOrDefault("realm_access", Collections.emptyMap());
            List<String> roles = (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());

            mappedAuthorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .toList());

            // Объединяем стандартные полномочия (SCOPES) с нашими новыми ролями
            mappedAuthorities.addAll(oidcUser.getAuthorities());
            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
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

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(jwkSetUri).build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(redirectUrl); // Замените на ваш URL

        return oidcLogoutSuccessHandler;
    }
}

