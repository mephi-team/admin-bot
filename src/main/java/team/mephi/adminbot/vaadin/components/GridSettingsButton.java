package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class GridSettingsButton extends Button {
    public GridSettingsButton() {
        setIcon(VaadinIcon.COG.create());
        addThemeVariants(ButtonVariant.LUMO_ICON);
        setAriaLabel("Show / hide columns");
    }
}
