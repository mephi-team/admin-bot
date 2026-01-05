package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.UserService;

import java.util.*;

public class TutoringDialog  extends Dialog  {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    private SerializableConsumer<SimpleUser> onSaveCallback;
    private SimpleUser user;

    public TutoringDialog(UserService userService) {
        var form = new TutorForm(userService);
        binder.bindInstanceFields(form);
        setHeaderTitle("dialog_tutor_curatorship_title");
        add(form);
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);
        binder.forField(form.getComboBox())
              .withConverter(s -> {
                  if (Objects.isNull(s)) return new ArrayList<SimpleUser>();
                  return s.stream().toList();
              }, e -> {
                  if (Objects.isNull(e)) return new HashSet<>();
                  return new HashSet<>(e);
              })
              .bind(SimpleUser::getStudents, SimpleUser::setStudents);
    }

    public void openForView(SimpleUser user) {
        binder.readBean(user);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        open();
    }

    public void openForEdit(SimpleUser user, SerializableConsumer<SimpleUser> callback) {
        this.user = user;
        this.onSaveCallback = callback;
        binder.readBean(user);
//        binder.setReadOnly(true);
//        saveButton.setVisible(false);
        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                binder.writeBeanIfValid(user);
                onSaveCallback.accept(user);
            }
            close();
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
