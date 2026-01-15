package team.mephi.adminbot.service;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * Сервис получения информации о пользователе.
 */
public interface AuthService {
    /**
     * Получает информацию о текущем пользователе.
     *
     * @return объект DefaultOidcUser с информацией о пользователе
     */
    DefaultOidcUser getUserInfo();

    /**
     * Проверяет, является ли текущий пользователь администратором.
     *
     * @return true, если пользователь является администратором, иначе false
     */
    boolean isAdmin();

    /**
     * Выполняет выход пользователя из системы.
     */
    void logout();
}
