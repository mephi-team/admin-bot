package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SecondaryButton extends Button {

    public SecondaryButton(String text) {
        super(text);
        init();
    }

    public SecondaryButton(String text, Component icon) {
        super(text, icon);
        init();
    }

    public SecondaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        init();
    }

    public SecondaryButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, icon, clickListener);
        init();
    }

    private void init() {
        addClassNames("border-12", LumoUtility.Background.TRANSPARENT, LumoUtility.Border.ALL, LumoUtility.BorderColor.PRIMARY);
        setMinHeight("40px");
    }

}
