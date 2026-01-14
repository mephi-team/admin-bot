package team.mephi.adminbot.vaadin.analytics.components;

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

        form.addFormItem(new ComboBox<String>(), getTranslation("page_analytics_form_activity_source_label"));

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel(getTranslation("page_analytics_form_activity_source_details_label"));
        form.add(checkbox);

        add(form);
    }
}
