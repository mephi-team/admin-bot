package team.mephi.adminbot.vaadin.components.fields;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * Компонент текстового поля для поиска с иконкой и возможностью очистки.
 */
public class SearchField extends TextField {
    /**
     * Конструктор для создания компонента SearchField с указанным плейсхолдером.
     *
     * @param placeholder текст плейсхолдера.
     */
    public SearchField(String placeholder) {
        setWidth("50%");
        setPlaceholder(placeholder);
        setPrefixComponent(VaadinIcon.SEARCH.create());
        setValueChangeMode(ValueChangeMode.EAGER);
        setClearButtonVisible(true);
    }
}
