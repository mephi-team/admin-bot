package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SearchFragment extends HorizontalLayout {
    public SearchFragment(Component left, Component right) {
        add(left, right);
        setAlignItems(FlexComponent.Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setFlexGrow(0.1, left);
        setFlexGrow(0, right);
        setWidthFull();
    }
}
