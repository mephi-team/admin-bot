package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Группа кнопок, выровненных по правому краю.
 */
public class ButtonGroup extends Div {
    public ButtonGroup(Component... components) {
        super(components);
        addClassNames(LumoUtility.TextAlignment.RIGHT);
    }
}
