package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nullable;
import team.mephi.adminbot.model.User;

public class UserDrawer extends Section {

    private final SerializableFunction<User, User> onSaveCallback;
    private final SerializableRunnable onCloseCallback;
    private final UserForm form;
    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    public UserDrawer(SerializableFunction<User, User> onSaveCallback,
                      SerializableRunnable onCloseCallback) {
        getElement().getStyle().set("position", "fixed");
        getElement().getStyle().set("top", "0");
        getElement().getStyle().set("right", "0");
        getElement().getStyle().set("bottom", "0");
        getElement().getStyle().set("z-index", "1");
        getElement().getStyle().set("background-color", "white");

        this.onSaveCallback = onSaveCallback;
        this.onCloseCallback = onCloseCallback;

        // Create the components
        form = new UserForm();

        HorizontalLayout header = new HorizontalLayout();
        var title = new H2("Редактировать пользователя");
        title.setId("proposal-drawer-header");
//        setAriaLabeledBy("proposal-drawer-header");

        var closeBtn = new Button(new Icon(VaadinIcon.CLOSE), event -> close());

        header.add(title, closeBtn);

        var saveBtn = new Button("Save", event -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var buttons = new HorizontalLayout(saveBtn);

        // Configure the drawer
        add(header, form, buttons);
        addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Border.ALL,
                LumoUtility.Padding.MEDIUM);
        setVisible(false);

        binder.bindInstanceFields(form);
        binder.setBean(new User());
    }

    public void setProposal(@Nullable User proposal) {
//        form.setFormDataObject(proposal);
        setVisible(proposal != null);
    }

    private void save() {
        if (binder.validate().isOk()) {
            User user = binder.getBean();
            // Сохранить в БД
//            Notification.show("Сохранено: " + user, 3000, Notification.Position.TOP_END);
        } else {
//            Notification.show("Исправьте ошибки", 3000, Notification.Position.MIDDLE);
        }
//        form.getFormDataObject().ifPresent(proposal -> {
//            var savedProposal = onSaveCallback.apply(proposal);
//            form.setFormDataObject(savedProposal);
//        });
    }

    private void close() {
        onCloseCallback.run();
    }
}
