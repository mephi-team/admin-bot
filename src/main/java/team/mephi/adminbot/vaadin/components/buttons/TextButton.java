package team.mephi.adminbot.vaadin.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class TextButton extends Button {
    public TextButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        init();
    }

    public TextButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, icon, clickListener);
        init();
    }

    private void init() {
        addClassNames(LumoUtility.Background.TINT, LumoUtility.Border.ALL, LumoUtility.BorderColor.PRIMARY, LumoUtility.BorderRadius.FULL, LumoUtility.Margin.Right.SMALL);
        addThemeVariants(ButtonVariant.LUMO_SMALL);
    }
}
