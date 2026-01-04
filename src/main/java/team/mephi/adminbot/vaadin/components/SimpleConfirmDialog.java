package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import lombok.Setter;

public class SimpleConfirmDialog extends ConfirmDialog {
    private final String header;
    private final String text;
    private final String headerAll;
    private final String textAll;
    @Getter
    @Setter
    private Runnable onConfirm;

    public SimpleConfirmDialog(String title, String text, String action) {
        this(title, text, action, "", "", null);
    }

    public SimpleConfirmDialog(String title, String text, String action, String titleAll, String textAll) {
        this(title, text, action, titleAll, textAll, null);
    }

    public SimpleConfirmDialog(String title, String text, String action, String titleAll, String textAll, Runnable onConfirm) {
        this.header = title;
        this.text = text;
        this.headerAll = titleAll;
        this.textAll = textAll;
        this.onConfirm = onConfirm;

        var closeButton = new Button(VaadinIcon.CLOSE_BIG.create() , e -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        closeButton.addClassNames(LumoUtility.AlignSelf.END, LumoUtility.Background.TINT, LumoUtility.TextColor.BODY);
        var headerText = new Div(getTranslation(title));
        headerText.addClassNames(LumoUtility.FontWeight.BOLD);
        headerText.setWidthFull();
        var icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
        icon.addClassNames(LumoUtility.TextColor.WARNING);
        var header = new Div(icon, headerText, closeButton);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.CENTER);
        setHeader(header);
        setText(getTranslation(text));
        var confirmButton = new Button(getTranslation(action), VaadinIcon.TRASH.create());
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setConfirmButton(confirmButton);

        setCancelable(true);
        setCancelText(getTranslation("cancel_button"));
        addCancelListener(event -> {
            close();
        });

        addConfirmListener(e -> {
            if (getOnConfirm() != null) getOnConfirm().run();
        });
    }

    public void showForConfirm(int count, Runnable onConfirm) {
        if (count > 1) {
            setHeader(getTranslation(this.headerAll));
            setText(getTranslation(textAll, count));
        } else {
            setHeader(getTranslation(this.header));
            setText(getTranslation(this.text));
        }
        this.onConfirm = onConfirm;
        open();
    }
}
