package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class UtmFilterSource extends HorizontalLayout {
    public UtmFilterSource() {
        setWidthFull();

        var form = new FormLayout();
        form.setWidthFull();
        form.setAutoResponsive(true);
        form.setExpandColumns(true);
        form.setExpandFields(true);

        form.addFormItem(new ComboBox<String>(), "Источник");

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Детализировать по источникам");
        form.add(checkbox);

        add(form);
    }
}
