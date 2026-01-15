package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Кнопка для открытия/закрытия бокового меню.
 */
public class MenuButton extends Button {
    /**
     * Создает кнопку меню с заданным слушателем кликов.
     *
     * @param onClick Слушатель событий клика по кнопке.
     */
    public MenuButton(ComponentEventListener<ClickEvent<Button>> onClick) {
        setIcon(VaadinIcon.MENU.create());
        addClickListener(onClick);
        getElement().setAttribute("aria-label", "Toggle drawer minimization");
        setMaxWidth("68px");
        setMinHeight("48px");
        getStyle().set("--vaadin-button-border-radius", "16px");
        getStyle().set("--vaadin-button-background", "#d3e1f9");
        getStyle().set("--_lumo-button-text-color", "#162143");
        getStyle().set("cursor", "pointer");
    }
}
