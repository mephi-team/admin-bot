package team.mephi.adminbot.service;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * Сервис получения информации о пользователе.
 */
public interface AuthService {
    DefaultOidcUser getUserInfo();

    boolean isAdmin();

    void logout();
}
