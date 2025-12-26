package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

import java.util.List;

public class TemplateFormTab extends FormLayout {
    private final TextField id = new TextField();
    @Getter
    private final TextField name = new TextField();
    @Getter
    private final TextArea text = new TextArea();

    public TemplateFormTab() {
        setAutoResponsive(true);
        setLabelsAside(true);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("Создать новое", "Загрузить из шаблона");
        radioGroup.setValue("Создать новое");

        ComboBox<String> templates = new ComboBox<>();
        templates.setItems(List.of("Шаблон 1", "Шаблон 2"));

        Checkbox createLink = new Checkbox();
        Checkbox saveTemplate = new Checkbox();

        TextField link = new TextField();
        link.setValue("https://telemost.360.yandex.ru/j/000000000");

        text.setMinRows(10);

        addFormItem(radioGroup, "Сообщение");
        FormItem templateItem = addFormItem(templates, "Шаблон");
        FormItem textItem = addFormItem(text, getTranslation("template_form_name_label"));
        FormItem createLinkItem = addFormItem(createLink, "Создать встречу");
        FormItem linkItem = addFormItem(link, "Ссылка на встречу");
        FormItem saveItem = addFormItem(saveTemplate, "Сохранить как новый шаблон");
        FormItem nameItem = addFormItem(name, getTranslation("template_form_name_label"));

        templateItem.setVisible(false);
        nameItem.setVisible(false);
        linkItem.setVisible(false);
        linkItem.setEnabled(false);

        templates.addValueChangeListener(v -> {
           text.setValue(v.getValue());
        });

        createLink.addValueChangeListener(v -> linkItem.setVisible(v.getValue()));
        saveTemplate.addValueChangeListener(v -> nameItem.setVisible(v.getValue()));

        radioGroup.addValueChangeListener(v -> {
            if (v.getValue().equals("Создать новое")) {
                templateItem.setVisible(false);
                createLinkItem.setVisible(true);
                linkItem.setVisible(true);
                saveItem.setVisible(true);
                if (saveTemplate.getValue()) {
                    nameItem.setVisible(true);
                }
                textItem.setEnabled(true);
            } else {
                templateItem.setVisible(true);
                createLinkItem.setVisible(false);
                linkItem.setVisible(false);
                saveItem.setVisible(false);
                nameItem.setVisible(false);
                textItem.setEnabled(false);
            }
        });
    }
}
