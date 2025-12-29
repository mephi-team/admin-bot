package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class UserCountBadge extends Span {
    public UserCountBadge(Long value) {
        setText(String.valueOf(value));
        getElement().getThemeList().add("badge small contrast");
        addClassNames(LumoUtility.Margin.Left.XSMALL);
    }

    public void setCount(Long value) {
        setText(String.valueOf(value));
    }
}
