package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import lombok.Setter;

public class SimpleConfirmDialog extends ConfirmDialog {
    private final String header;
    private final String text;
    private final Div headerText = new Div();
    @Getter
    @Setter
    private Runnable onConfirm;

    public SimpleConfirmDialog(String title, String text, String action) {
        this(title, text, action, VaadinIcon.TRASH.create(), null);
    }

    public SimpleConfirmDialog(String title, String text, String action, Icon actionIcon, Runnable onConfirm) {
        this.header = title;
        this.text = text;
        this.onConfirm = onConfirm;

        var closeButton = new Button(VaadinIcon.CLOSE_BIG.create() , e -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        closeButton.addClassNames(LumoUtility.AlignSelf.END, LumoUtility.Background.TINT, LumoUtility.TextColor.BODY);
        headerText.addClassNames(LumoUtility.FontWeight.BOLD);
        headerText.setWidthFull();
        var icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
        icon.addClassNames(LumoUtility.TextColor.WARNING);
        var header = new Div(icon, headerText, closeButton);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.CENTER);
        setHeader(header);
        setText(getTranslation(text));
        var confirmButton = new Button(getTranslation(action), actionIcon);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        confirmButton.addClassNames(LumoUtility.BorderRadius.FULL);
        setConfirmButton(confirmButton);

        setCancelable(true);
        var cancelButton = new Button(getTranslation("cancel_button"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        cancelButton.addClassNames(LumoUtility.BorderRadius.FULL, LumoUtility.Border.ALL, LumoUtility.BorderColor.PRIMARY);
        setCancelButton(cancelButton);
        addCancelListener(event -> {
            close();
        });

        addConfirmListener(e -> {
            if (getOnConfirm() != null) getOnConfirm().run();
        });
    }

    public void showForConfirm(Object count, Runnable onConfirm) {
        headerText.setText(getTranslation(this.header, count));
        setText(getTranslation(this.text, count));
        this.onConfirm = onConfirm;
        open();
    }
}
