package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import java.util.function.Consumer;

public class UserConfirmDialog extends ConfirmDialog {
    private final String header;
    private final String text;
    private final String headerAll;
    private final String textAll;
    private final String message;
    private final String messageAll;
    private int count;

    public UserConfirmDialog(String title, String text, String action, String titleAll, String textAll, String message, String messageAll, Consumer<String> onConfirm) {
        this.header = title;
        this.text = text;
        this.headerAll = titleAll;
        this.textAll = textAll;
        this.message = message;
        this.messageAll = messageAll;

        setHeader(title);
        setText(text);
        setConfirmText(action);

        setCancelable(true);
        setCancelText("Отмена");
        addCancelListener(event -> {
            close();
        });

        addConfirmListener(e -> {
            onConfirm.accept(getMessageText());
        });
    }

    public void setCount(int count) {
        this.count = count;
        if (count == 1) {
            setHeader(this.header);
            setText(this.text);
        } else {
            setHeader(this.headerAll);
            setText(String.format(textAll, count));
        }
    }

    private String getMessageText() {
        if (count == 1) {
            return message;
        } else {
            return String.format(messageAll, count);
        }
    }
}
