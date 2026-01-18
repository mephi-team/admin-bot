package team.mephi.adminbot.config;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.*;

/**
 * Конфигурация безопасности Vaadin с использованием OAuth2 и OIDC (например, Keycloak).
 */
@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class VaadinSecurityConfig {

    @Value("${spring.security.oauth2.client.provider.keycloak.end-session-uri}")
    String keycloakLogoutUrl;

    @Value("${app.redirect-url}")
    String postLogoutRedirectUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.keycloak.jwk-set-uri}")
    String jwkSetUri;

    /**
     * Настраивает цепочку фильтров безопасности для API с поддержкой JWT.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception в случае ошибки настройки
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .oauth2Login(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // Возвращаем 401 вместо редиректа
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((ignoredRequest, res, ignoredException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                );
        return http.build();
    }

    /**
     * Конвертер для преобразования JWT в JwtAuthenticationToken с учетом clientId.
     *
     * @return настроенный конвертер JWT
     */
    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter(clientId);
    }

    /**
     * Настраивает декодер JWT с использованием JWK Set URI.
     *
     * @return настроенный декодер JWT
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Настраивает цепочку фильтров безопасности для Vaadin с поддержкой OAuth2 и OIDC.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception в случае ошибки настройки
     */
    @Bean
    @Order(2)
    SecurityFilterChain vaddinSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                // Настраиваем сервис для получения информации о пользователе и ролях
                                .userInfoEndpoint(userInfo ->
                                        userInfo.oidcUserService(oidcUserService())
                                )
                ).logout(logout ->
                        logout
                                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                                .invalidateHttpSession(true)
                )
                .with(
                        VaadinSecurityConfigurer.vaadin(), ignoredConfigurer -> {
//                            configurer.loginView(LoginView.class);
                        });

        return http.build();
    }

    /**
     * Обработчик успешного выхода из системы для OIDC.
     * Перенаправляет пользователя на страницу выхода Keycloak с указанием URL для возврата после выхода.
     *
     * @return обработчик успешного выхода
     */
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        return (ignoredRequest, response, authentication) -> {
            String redirectUrl = keycloakLogoutUrl + "?post_logout_redirect_uri=" + postLogoutRedirectUri;

            // Если пользователь аутентифицирован, можно добавить id_token_hint для авто-выхода без подтверждения
            if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                String idToken = oidcUser.getIdToken().getTokenValue();
                redirectUrl += "&id_token_hint=" + idToken;
            }

            response.sendRedirect(redirectUrl);
        };
    }

    /**
     * Настраивает сервис для получения информации о пользователе OIDC и его ролях из токена доступа.
     *
     * @return настроенный сервис для получения информации о пользователе OIDC
     */
    @SuppressWarnings("unchecked")
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
}
