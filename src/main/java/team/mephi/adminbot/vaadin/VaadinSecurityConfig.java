package team.mephi.adminbot.vaadin;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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

import java.io.IOException;
import java.util.*;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class VaadinSecurityConfig {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
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
                        VaadinSecurityConfigurer.vaadin(), configurer -> {
//                            configurer.loginView(LoginView.class);
                        });

        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080/v2"); // Замените на ваш URL

        return oidcLogoutSuccessHandler;
    }

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
