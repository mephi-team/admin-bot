package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Кнопка, отображающая только иконку.
 */
public class IconButton extends Button {

    /**
     * Создает кнопку с указанной иконкой.
     *
     * @param icon Компонент иконки для отображения на кнопке.
     */
    public IconButton(Component icon) {
        super(icon);
        init();
    }

    /**
     * Создает кнопку с указанной иконкой и слушателем кликов.
     *
     * @param icon          Компонент иконки для отображения на кнопке.
     * @param clickListener Слушатель событий клика по кнопке.
     */
    public IconButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon, clickListener);
        init();
    }

    private void init() {
        addClassNames(LumoUtility.TextColor.BODY, LumoUtility.Background.TRANSPARENT, LumoUtility.Padding.NONE, LumoUtility.Margin.Right.XSMALL);
    }
}
