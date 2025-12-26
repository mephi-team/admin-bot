package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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

        setHeader(getTranslation(title));
        setText(getTranslation(text));
        setConfirmText(getTranslation(action));

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
