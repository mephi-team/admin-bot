package team.mephi.adminbot.vaadin.components.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Objects;

/**
 * Компонент для ввода полного имени с полями для имени и фамилии.
 */
public class FullNameField extends CustomField<FullNameField.FullName> {
    private final TextField firstName = new TextField();
    private final TextField lastName = new TextField();

    public FullNameField() {
        firstName.setMaxWidth("50%");
        lastName.setMaxWidth("50%");

        lastName.addClassName("first");
        firstName.addClassName("last");

        firstName.setManualValidation(true);
        lastName.setManualValidation(true);

        firstName.addValueChangeListener(e -> updateValue());
        lastName.addValueChangeListener(e -> updateValue());

        var content = new Div(lastName, firstName);
        content.addClassNames(LumoUtility.JustifyContent.BETWEEN);
        content.addClassNames(LumoUtility.Display.FLEX);

        add(content);
    }

    @Override
    protected FullName generateModelValue() {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return null;
        }
        return new FullName(firstName.getValue(), lastName.getValue());
    }

    @Override
    protected void setPresentationValue(FullName fullName) {
        this.firstName.setValue(Objects.isNull(fullName.firstName()) ? "" : fullName.firstName());
        this.lastName.setValue(Objects.isNull(fullName.lastName()) ? "" : fullName.lastName());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        firstName.setReadOnly(readOnly);
        lastName.setReadOnly(readOnly);
    }

    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
        firstName.setInvalid(invalid);
        lastName.setInvalid(invalid);
    }

    public record FullName(String firstName, String lastName) {
    }
}

