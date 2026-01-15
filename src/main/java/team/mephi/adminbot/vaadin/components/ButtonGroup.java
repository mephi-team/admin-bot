package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Группа кнопок, выровненных по правому краю.
 */
public class ButtonGroup extends Div {
    /**
     * Создает группу кнопок с заданными компонентами.
     *
     * @param components Компоненты, которые будут добавлены в группу кнопок.
     */
    public ButtonGroup(Component... components) {
        super(components);
        addClassNames(LumoUtility.TextAlignment.RIGHT);
    }
}
