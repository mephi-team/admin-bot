package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Span;

public class UserCountBadge extends Span {
    public UserCountBadge(Long value) {
        setText(String.valueOf(value));
        getElement().getThemeList().add("badge small contrast");
        getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
    }

    public void setCount(Long value) {
        setText(String.valueOf(value));
    }
}
