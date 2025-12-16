package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GridSelectActions extends HorizontalLayout {
    Span countControl;
    private Integer selectedCount = 0;

    public GridSelectActions(Component... components) {
        countControl = new Span(String.valueOf(selectedCount));
        Span prefix = new Span(new Span("Выбрано пользователей: "), countControl);
        prefix.getElement().getStyle().set("font-weight", "bold");
        setAlignItems(Alignment.CENTER);

        add(prefix);
        add(components);
        setVisible(false);
    }

    public void setCount(Integer count) {
        selectedCount = count;
        countControl.setText(String.valueOf(selectedCount));
        setVisible(count > 0);
    }
}
