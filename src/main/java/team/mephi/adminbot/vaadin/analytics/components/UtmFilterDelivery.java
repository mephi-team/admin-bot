package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Форма для выбора параметров доставки на странице аналитики.
 */
public class UtmFilterDelivery extends HorizontalLayout {
    public UtmFilterDelivery() {
        setWidthFull();

        var form = new FormLayout();
        form.setWidthFull();
        form.setAutoResponsive(true);
        form.setExpandColumns(true);
        form.setExpandFields(true);

        form.addFormItem(new ComboBox<String>(), getTranslation("page_analytics_form_activity_delivery_label"));

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel(getTranslation("page_analytics_form_activity_delivery_details_label"));
        form.add(checkbox);

        add(form);
    }
}
