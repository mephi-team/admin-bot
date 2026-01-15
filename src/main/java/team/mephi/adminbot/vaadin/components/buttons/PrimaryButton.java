package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

/**
 * Основная кнопка приложения с предустановленным стилем.
 */
public class PrimaryButton extends Button {

    /**
     * Создает основную кнопку с заданным текстом.
     *
     * @param text Текст кнопки.
     */
    public PrimaryButton(String text) {
        super(text);
        init();
    }

    /**
     * Создает основную кнопку с заданным текстом и слушателем кликов.
     *
     * @param text          Текст кнопки.
     * @param clickListener Слушатель событий клика по кнопке.
     */
    public PrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        init();
    }

    /**
     * Создает основную кнопку с заданным текстом и иконкой.
     *
     * @param text Текст кнопки.
     * @param icon Иконка кнопки.
     */
    public PrimaryButton(String text, Component icon) {
        super(text, icon);
        init();
    }

    /**
     * Создает основную кнопку с заданным текстом, иконкой и слушателем кликов.
     *
     * @param text          Текст кнопки.
     * @param icon          Иконка кнопки.
     * @param clickListener Слушатель событий клика по кнопке.
     */
    public PrimaryButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, icon, clickListener);
        init();
    }

    private void init() {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addClassNames("border-12");
        setMinHeight("40px");
    }
}
