package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.FullNameField;
import team.mephi.adminbot.vaadin.components.PrimaryButton;
import team.mephi.adminbot.vaadin.components.SecondaryButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class TutoringDialog extends Dialog implements SimpleDialog {
    private final BeanValidationBinder<SimpleTutor> binder = new BeanValidationBinder<>(SimpleTutor.class);
    private SerializableConsumer<SimpleTutor> onSaveCallback;
    private SimpleTutor user;
    private final Button saveButton = new PrimaryButton(getTranslation("save_button"), e -> onSave());

    public TutoringDialog(UserService userService, DirectionService directionService) {
        var form = new TutorForm(userService, directionService);
        setHeaderTitle("dialog_tutor_curatorship_title");
        add(form);
        setWidth("100%");
        setMaxWidth("500px");
        getFooter().add(new SecondaryButton(getTranslation("cancel_button"), e -> close()), saveButton);
        binder.forField(form.getFullNameField())
                .bind(s -> new FullNameField.FullName(s.getFirstName(), s.getLastName()),
                        (s, t) -> {
                            s.setFirstName(t.firstName());
                            s.setLastName(t.lastName());
                        });
        binder.forField(form.getDirections())
                .withConverter(s -> {
                    if (Objects.isNull(s)) return new ArrayList<SimpleDirection>();
                    return s.stream().toList();
                }, e -> {
                    if (Objects.isNull(e)) return new HashSet<>();
                    return new HashSet<>(e);
                })
                .bind(SimpleTutor::getDirections, SimpleTutor::setDirections);
        binder.forField(form.getStudents())
                .withConverter(s -> {
                    if (Objects.isNull(s)) return new ArrayList<SimpleUser>();
                    return s.stream().toList();
                }, e -> {
                    if (Objects.isNull(e)) return new HashSet<>();
                    return new HashSet<>(e);
                })
                .bind(SimpleTutor::getStudents, SimpleTutor::setStudents);
        binder.bindInstanceFields(form);
    }

    @Override
    public void showDialog(Object user, SerializableConsumer<?> callback) {
        this.user = (SimpleTutor) user;
        this.onSaveCallback = (SerializableConsumer<SimpleTutor>) callback;
        binder.readBean((SimpleTutor) user);
//        binder.setReadOnly(true);
//        saveButton.setVisible(false);
        open();
    }

    private void onSave() {
        if (binder.validate().isOk()) {
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
