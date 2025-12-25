package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import lombok.Getter;
import lombok.Setter;

public class UserConfirmDialog extends ConfirmDialog {
    private final String header;
    private final String text;
    private final String headerAll;
    private final String textAll;
    @Getter
    @Setter
    private Runnable onConfirm;

    public UserConfirmDialog(String title, String text, String action, String titleAll, String textAll, Runnable onConfirm) {
        this.header = title;
        this.text = text;
        this.headerAll = titleAll;
        this.textAll = textAll;
        this.onConfirm = onConfirm;

        setHeader(title);
        setText(text);
        setConfirmText(action);

        setCancelable(true);
        setCancelText("Отмена");
        addCancelListener(event -> {
            close();
        });

        addConfirmListener(e -> {
            if (getOnConfirm() != null) getOnConfirm().run();
        });
    }

    public void showForConfirm(int count, Runnable onConfirm) {
        if (count > 1) {
            setHeader(this.headerAll);
            setText(String.format(textAll, count));
        } else {
            setHeader(this.header);
            setText(this.text);
        }
        this.onConfirm = onConfirm;
        open();
    }
}
