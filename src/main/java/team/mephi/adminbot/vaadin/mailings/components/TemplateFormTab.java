package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.service.TemplateService;

/**
 * Вкладка формы для создания и редактирования шаблонов рассылок.
 */
public class TemplateFormTab extends FormLayout {
    @Getter
    private final TextField name1 = new TextField();
    @Getter
    private final TextArea text1 = new TextArea();

    /**
     * Конструктор вкладки формы шаблона.
     *
     * @param templateService сервис для работы с шаблонами
     */
    public TemplateFormTab(TemplateService templateService) {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        Tabs group = new Tabs();
        group.addClassNames(LumoUtility.Margin.Vertical.SMALL);
        group.addThemeNames("button-group");
        var newMessage = new Tab(getTranslation("form_mailing_message_new_message_label"));
        var fromTemplate = new Tab(getTranslation("form_mailing_message_from_template_label"));
        group.add(newMessage, fromTemplate);

        ComboBox<SimpleTemplate> templates = new ComboBox<>();
        templates.setItemsPageable(templateService::findAll);
        templates.setItemLabelGenerator(SimpleTemplate::getName);

        Checkbox createLink = new Checkbox();
        Checkbox saveTemplate = new Checkbox();

        TextField link = new TextField();
        link.setValue("https://telemost.360.yandex.ru/j/000000000");

        text1.setMinRows(10);
        text1.setRequiredIndicatorVisible(true);
        text1.setAutofocus(true);

        addFormItem(group, getTranslation("form_mailing_message_label"));
        FormItem templateItem = addFormItem(templates, getTranslation("form_mailing_template_label"));
        addFormItem(text1, getTranslation("form_template_text_label"));
        FormItem createLinkItem = addFormItem(createLink, getTranslation("form_mailing_create_meeting_label"));
        FormItem linkItem = addFormItem(link, getTranslation("form_mailing_meeting_link_label"));
        FormItem saveItem = addFormItem(saveTemplate, getTranslation("form_mailing_save_as_new_label"));
        FormItem nameItem = addFormItem(name1, getTranslation("form_template_name_label"));

        templateItem.setVisible(false);
        nameItem.setVisible(false);
        linkItem.setVisible(false);
        link.setReadOnly(true);

        templates.addValueChangeListener(v -> {
            name1.setValue(v.getValue().getName());
            text1.setValue(v.getValue().getText());
        });

        createLink.addValueChangeListener(v -> linkItem.setVisible(v.getValue()));
        saveTemplate.addValueChangeListener(v -> nameItem.setVisible(v.getValue()));

        group.addSelectedChangeListener(v -> {
            if (v.getSelectedTab().equals(newMessage)) {
                templateItem.setVisible(false);
                createLinkItem.setVisible(true);
                if (createLink.getValue()) {
                    linkItem.setVisible(true);
                }
                saveItem.setVisible(true);
                if (saveTemplate.getValue()) {
                    nameItem.setVisible(true);
                }
                text1.setReadOnly(false);
            } else {
                templateItem.setVisible(true);
                createLinkItem.setVisible(false);
                linkItem.setVisible(false);
                saveItem.setVisible(false);
                nameItem.setVisible(false);
                text1.setReadOnly(true);
            }
        });
    }
}
