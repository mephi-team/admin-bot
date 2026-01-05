package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.UserService;

public class TutoringDialog  extends Dialog  {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    @Setter
    private SerializableRunnable onSaveCallback;

    public TutoringDialog(UserService userService) {
        var form = new TutorForm(userService);
        binder.bindInstanceFields(form);
        setHeaderTitle("dialog_tutor_curatorship_title");
        add(form);
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);
    }

    public void openForView(SimpleUser user) {
        binder.readBean(user);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
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

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
