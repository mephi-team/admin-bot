package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Второстепенная кнопка, отображающая только иконку.
 */
public class SecondaryIconButton extends Button {

    public SecondaryIconButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon, clickListener);
        init();
    }

    private void init() {
        setWidth("40px");
        addClassNames(LumoUtility.BorderRadius.FULL, LumoUtility.Border.ALL, LumoUtility.Background.TRANSPARENT, LumoUtility.BorderColor.PRIMARY, LumoUtility.Padding.NONE, LumoUtility.Margin.Right.XSMALL);
        addThemeVariants(ButtonVariant.LUMO_SMALL);
    }
}
