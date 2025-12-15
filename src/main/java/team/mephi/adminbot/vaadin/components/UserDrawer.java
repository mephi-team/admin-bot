package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nullable;
import team.mephi.adminbot.model.User;

public class UserDrawer extends Section {

    private final SerializableFunction<User, User> onSaveCallback;
    private final SerializableRunnable onCloseCallback;
    private final UserForm form;

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

        var header = new H2("Редактировать пользователя");
        header.setId("proposal-drawer-header");
//        setAriaLabeledBy("proposal-drawer-header");

        var saveBtn = new Button("Save", event -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var closeBtn = new Button("Close", event -> close());

        var buttons = new HorizontalLayout(closeBtn, saveBtn);

        // Configure the drawer
        add(header, form, buttons);
        addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Border.ALL,
                LumoUtility.Padding.MEDIUM);
        setVisible(false);
    }

    public void setProposal(@Nullable User proposal) {
//        form.setFormDataObject(proposal);
        setVisible(proposal != null);
    }

    private void save() {
//        form.getFormDataObject.ifPresent(proposal -> {
//            var savedProposal = onSaveCallback.apply(proposal);
//            form.setFormDataObject(savedProposal);
//        });
    }

    private void close() {
        onCloseCallback.run();
    }
}
