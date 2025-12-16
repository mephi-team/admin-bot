package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class RightDrawer extends Section {

    private final SerializableSupplier<Boolean>  onSaveCallback;
    private final SerializableRunnable onCloseCallback;
    private final Button saveBtn;

    public RightDrawer(String title, FormLayout form, SerializableSupplier<Boolean> onSaveCallback, SerializableRunnable onCloseCallback) {
        this.onSaveCallback = onSaveCallback;
        this.onCloseCallback = onCloseCallback;

        getElement().getStyle().set("position", "fixed");
        getElement().getStyle().set("top", "0");
        getElement().getStyle().set("right", "0");
        getElement().getStyle().set("bottom", "0");
        getElement().getStyle().set("z-index", "1");
        getElement().getStyle().set("background-color", "white");

        var header = new HorizontalLayout();
        header.add(new H2(title), new Button(new Icon(VaadinIcon.CLOSE), this::close));

        saveBtn = new Button("Сохранить", this::save);
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var buttons = new HorizontalLayout(saveBtn);

        // Configure the drawer
        add(header, form, buttons);
        addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Border.ALL,
                LumoUtility.Padding.MEDIUM);
        setVisible(false);
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if (onSaveCallback.get()) {
            setVisible(false);
        }
    }

    private void close(ClickEvent<Button> buttonClickEvent) {
        onCloseCallback.run();
        setVisible(false);
    }
}
