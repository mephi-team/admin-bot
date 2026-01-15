package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Правый драйвер с формой.
 */
@SuppressWarnings("unused")
public class RightDrawer extends Section {

    private final SerializableSupplier<Boolean> onSaveCallback;
    private final SerializableRunnable onCloseCallback;

    /**
     * Создает правый драйвер с заданным заголовком, формой и обратными вызовами для сохранения и закрытия.
     *
     * @param title           Заголовок драйвера.
     * @param form            Форма, отображаемая в драйвере.
     * @param onSaveCallback  Обратный вызов, вызываемый при сохранении формы. Должен возвращать true, если сохранение прошло успешно.
     * @param onCloseCallback Обратный вызов, вызываемый при закрытии драйвера.
     */
    public RightDrawer(String title, FormLayout form, SerializableSupplier<Boolean> onSaveCallback, SerializableRunnable onCloseCallback) {
        this.onSaveCallback = onSaveCallback;
        this.onCloseCallback = onCloseCallback;

        addClassNames(LumoUtility.Position.FIXED, LumoUtility.ZIndex.SMALL, LumoUtility.Background.CONTRAST);
        getElement().getStyle().set("top", "0");
        getElement().getStyle().set("right", "0");
        getElement().getStyle().set("bottom", "0");

        var header = new HorizontalLayout();
        header.add(new H2(title), new Button(VaadinIcon.CLOSE.create(), this::close));

        Button saveBtn = new Button("Сохранить", this::save);
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

    /**
     * Открывает драйвер.
     */
    private void save(ClickEvent<Button> buttonClickEvent) {
        if (onSaveCallback.get()) {
            setVisible(false);
        }
    }

    /**
     * Закрывает драйвер.
     */
    private void close(ClickEvent<Button> buttonClickEvent) {
        onCloseCallback.run();
        setVisible(false);
    }
}
