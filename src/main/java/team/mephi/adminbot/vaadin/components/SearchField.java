package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class SearchField extends TextField {
    public SearchField(String placeholder) {
        setWidth("100%");
        setPlaceholder(placeholder);
        setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        setValueChangeMode(ValueChangeMode.EAGER);
    }
}
