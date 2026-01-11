package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class PrimaryButton extends Button {

    public PrimaryButton(String text) {
        super(text);
        init();
    }

    public PrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        init();
    }

    public PrimaryButton(String text, Component icon) {
        super(text, icon);
        init();
    }

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
