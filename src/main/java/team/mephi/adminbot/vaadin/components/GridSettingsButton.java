package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class GridSettingsButton extends Button {
    public GridSettingsButton() {
        setIcon(VaadinIcon.COG.create());
        addThemeVariants(ButtonVariant.LUMO_ICON);
        addClassNames(LumoUtility.TextColor.BODY, LumoUtility.Background.TINT, LumoUtility.Padding.NONE, LumoUtility.Margin.Right.XSMALL);
        setAriaLabel("Show / hide columns");
    }
}
