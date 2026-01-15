package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Второстепенная кнопка приложения с предустановленным стилем.
 */
public class SecondaryButton extends Button {

    /**
     * Создает второстепенную кнопку с заданным текстом.
     *
     * @param text Текст кнопки.
     */
    public SecondaryButton(String text) {
        super(text);
        init();
    }

    /**
     * Создает второстепенную кнопку с заданным текстом и иконкой.
     *
     * @param text Текст кнопки.
     * @param icon Иконка кнопки.
     */
    public SecondaryButton(String text, Component icon) {
        super(text, icon);
        init();
    }

    /**
     * Создает второстепенную кнопку с заданным текстом и слушателем кликов.
     *
     * @param text          Текст кнопки.
     * @param clickListener Слушатель событий клика по кнопке.
     */
    public SecondaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        init();
    }

    /**
     * Создает второстепенную кнопку с заданным текстом, иконкой и слушателем кликов.
     *
     * @param text          Текст кнопки.
     * @param icon          Иконка кнопки.
     * @param clickListener Слушатель событий клика по кнопке.
     */
    public SecondaryButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, icon, clickListener);
        init();
    }

    private void init() {
        addClassNames("border-12", LumoUtility.Background.TRANSPARENT, LumoUtility.Border.ALL, LumoUtility.BorderColor.PRIMARY);
        setMinHeight("40px");
    }

}
