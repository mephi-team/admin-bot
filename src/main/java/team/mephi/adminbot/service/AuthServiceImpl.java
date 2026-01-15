package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Реализация сервиса получения информации о пользователе.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationContext authContext;

    /**
     * Конструктор сервиса аутентификации.
     *
     * @param authContext контекст аутентификации.
     */
    public AuthServiceImpl(AuthenticationContext authContext) {
        this.authContext = authContext;
    }

    @Override
    public DefaultOidcUser getUserInfo() {
        return authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
    }

    @Override
    public boolean isAdmin() {
        Collection<String> userRoles = authContext.getGrantedRoles();
        return userRoles.contains("ADMIN");
    }

    @Override
    public void logout() {
        authContext.logout();
    }
}
