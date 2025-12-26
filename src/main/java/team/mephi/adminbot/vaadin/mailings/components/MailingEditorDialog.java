package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.UserDto;

import java.util.Objects;

public class MailingEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleMailing> binder = new BeanValidationBinder<>(SimpleMailing.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());
    private final TabSheet tabSheet = new TabSheet();
    private final Tab tab1;
    private final Tab tab2;
    private final Button next = new Button(getTranslation("next_button"), VaadinIcon.ARROW_RIGHT.create());
    private final Button prev = new Button(getTranslation("prev_button"), VaadinIcon.ARROW_LEFT.create());

    @Setter
    private SerializableRunnable onSaveCallback;

    public MailingEditorDialog(UserService userService) {
        var form1 = new MailingForm(userService);
        var form2 = new TemplateFormTab();

        binder.forField(form1.getUser())
                .withValidator(Objects::nonNull, "Пользователь обязателен")
                .withConverter(UserDto::getId, userId -> userService.getById(userId).orElse(null))
                .bind("userId");
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);

        tab1 = tabSheet.add(getTranslation("mailing_dialog_tab_recipients"), form1);
        tab2 = tabSheet.add(getTranslation("mailing_dialog_tab_message"), form2);

        setHeaderTitle("mailing_new_title");
        add(tabSheet);

        saveButton.setVisible(false);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.setIconAfterText(true);
        next.getStyle().set("margin-right", "auto");
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.addClickListener(s -> {
            tabSheet.setSelectedTab(tab2);
        });
        prev.getStyle().set("margin-right", "auto");
        prev.addClickListener(s -> {
            tabSheet.setSelectedTab(tab1);
        });
        prev.setVisible(false);
        getFooter().add(
                next,
                prev,
                saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));

        tabSheet.addSelectedChangeListener(l -> {
           if (tab1.isSelected()) {
               saveButton.setVisible(false);
               prev.setVisible(false);
               next.setVisible(true);
           } else if (tab2.isSelected()) {
               saveButton.setVisible(true);
               prev.setVisible(true);
               next.setVisible(false);
           }
        });
    }

    public void showDialogForNew() {
        var newMailing = new SimpleMailing();
        newMailing.setUserId(0L);
        binder.readBean(newMailing);
        binder.setReadOnly(false);
//        saveButton.setVisible(true);
        tabSheet.setSelectedTab(tab1);
        open();
    }

    public void showDialogForEdit(SimpleMailing mailing) {
        binder.readBean(mailing);
        binder.setReadOnly(false);
//        saveButton.setVisible(true);
        tabSheet.setSelectedTab(tab1);
        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            close();
        }
    }

    public SimpleMailing getEditedItem() {
        SimpleMailing mailing = new SimpleMailing();
        binder.writeBeanIfValid(mailing);
        return mailing;
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
