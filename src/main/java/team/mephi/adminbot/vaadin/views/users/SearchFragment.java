package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SearchFragment extends HorizontalLayout {
    public SearchFragment(Component left, Component right) {
        add(left, right);
        setAlignItems(FlexComponent.Alignment.BASELINE);
        setFlexGrow(1, left);
        setFlexGrow(0, right);
        setWidthFull();
    }
}
