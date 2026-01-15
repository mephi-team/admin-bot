package team.mephi.adminbot.vaadin.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

/**
 * Простой диалог подтверждения с настраиваемым заголовком, текстом и действием.
 */
public class SimpleConfirmDialog extends ConfirmDialog {
    private final String titleTemplate;
    private final String textTemplate;
    private final Div headerText = new Div();

    @Setter
    @Getter
    private Runnable onConfirm;

    /**
     * Конструктор для создания простого диалога подтверждения.
     *
     * @param title      заголовок диалога.
     * @param text       текст диалога.
     * @param action     текст кнопки подтверждения.
     * @param actionIcon иконка кнопки подтверждения.
     * @param onConfirm  действие, выполняемое при подтверждении.
     */
    public SimpleConfirmDialog(String title, String text, String action, Icon actionIcon, Runnable onConfirm) {
        this.titleTemplate = Objects.requireNonNull(title, "title");
        this.textTemplate = Objects.requireNonNull(text, "text");
        this.onConfirm = onConfirm;

        setHeader(buildHeader());
        setText(getTranslation(this.textTemplate));
        setConfirmButton(buildConfirmButton(action, actionIcon));
        setCancelButton(buildCancelButton());

        setCancelable(true);
        addCancelListener(ignored -> close());
        addConfirmListener(ignored -> {
            Optional.ofNullable(this.onConfirm).ifPresent(Runnable::run);
            close();
        });
    }

    /**
     * Показать диалог с указанным количеством для перевода.
     *
     * @param count     значение для подстановки в шаблоны.
     * @param onConfirm действие при подтверждении.
     */
    public void showForConfirm(Object count, Runnable onConfirm) {
        headerText.setText(getTranslation(this.titleTemplate, count));
        setText(getTranslation(this.textTemplate, count));
        this.onConfirm = onConfirm;
        open();
    }

    /**
     * Построить заголовок диалога.
     *
     * @return элемент заголовка.
     */
    private Div buildHeader() {
        var closeButton = createCloseButton();
        headerText.addClassNames(LumoUtility.FontWeight.BOLD);
        headerText.setWidthFull();

        var icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
        icon.addClassNames(LumoUtility.TextColor.WARNING);

        var header = new Div(icon, headerText, closeButton);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.CENTER);
        return header;
    }

    /**
     * Создать кнопку закрытия для заголовка.
     *
     * @return кнопка закрытия.
     */
    private Button createCloseButton() {
        var closeButton = new Button(VaadinIcon.CLOSE_BIG.create(), ignored -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        closeButton.addClassNames(LumoUtility.AlignSelf.END, LumoUtility.Background.TINT, LumoUtility.TextColor.BODY);
        return closeButton;
    }

    /**
     * Построить кнопку подтверждения.
     *
     * @param action     текст кнопки.
     * @param actionIcon иконка кнопки.
     * @return кнопка подтверждения.
     */
    private Button buildConfirmButton(String action, Icon actionIcon) {
        var confirmLabel = getTranslation(action);
        var confirmButton = actionIcon != null ? new Button(confirmLabel, actionIcon) : new Button(confirmLabel);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        confirmButton.addClassNames(LumoUtility.BorderRadius.FULL);
        return confirmButton;
    }

    /**
     * Построить кнопку отмены.
     *
     * @return кнопка отмены.
     */
    private Button buildCancelButton() {
        var cancelButton = new Button(getTranslation("cancel_button"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        cancelButton.addClassNames(LumoUtility.BorderRadius.FULL, LumoUtility.Border.ALL, LumoUtility.BorderColor.PRIMARY);
        return cancelButton;
    }
}
