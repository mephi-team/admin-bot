package team.mephi.adminbot.vaadin.core;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.server.HttpStatusCode;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.TopMenu;
import team.mephi.adminbot.vaadin.components.UserMenu;

/**
 * Кастомная страница ошибки доступа запрещен.
 */
@PermitAll
@SuppressWarnings("unused")
public class CustomAccessDeniedError extends AppLayout
        implements HasErrorParameter<AccessDeniedException> {

    /**
     * Конструктор для создания страницы ошибки доступа запрещен.
     *
     * @param authService сервис аутентификации для отображения пользовательского меню.
     */
    public CustomAccessDeniedError(AuthService authService) {
        addToNavbar(new Logo("Neoflex"), new TopMenu(authService), new UserMenu(authService));
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<AccessDeniedException> parameter) {
        getElement().setText("Access denied.");
        return HttpStatusCode.UNAUTHORIZED.getCode();
    }
}
