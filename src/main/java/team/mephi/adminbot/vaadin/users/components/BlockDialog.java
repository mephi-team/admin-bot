package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

/**
 * Диалоговое окно для блокировки пользователя с информацией и предупреждением.
 *
 * @param <T> тип объекта, связанного с блокировкой
 */
public class BlockDialog<T> extends Dialog implements DialogWithTitle {
    private final BeanValidationBinder<T> binder;
    private final Tabs tabs = new Tabs();
    private final Tab tab1 = new Tab(getTranslation("dialog_user_block_tab_warning_label"));
    private final Tab tab2 = new Tab(getTranslation("dialog_user_block_tab_block_label"));
    private final Class<T> beanType;
    private SerializableConsumer<T> onSaveCallback;
    private final Button saveButton = new Button(getTranslation("save_button"), ignoredEvent -> onSave());

    /**
     * Конструктор диалогового окна блокировки пользователя.
     *
     * @param beanType класс типа объекта, связанного с блокировкой
     */
    public BlockDialog(Class<T> beanType) {
        this.beanType = beanType;
        this.binder = new BeanValidationBinder<>(beanType);

        var form = new BlockUserInfo();
        var form1 = new WarningForm();
        var form2 = new BlockForm();
        var form3 = new BlockUserMessage();
        form2.setVisible(false);
        tabs.add(tab1, tab2);
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab().equals(tab1)) {
                form1.setVisible(true);
                form2.setVisible(false);
            } else if (e.getSelectedTab().equals(tab2)) {
                form1.setVisible(false);
                form2.setVisible(true);
            }
        });
        binder.bindInstanceFields(form);
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);

        setHeaderTitle("dialog_user_block_title");
        add(tabs, form, form1, form2, form3);
        setWidth("100%");
        setMaxWidth("500px");
        getFooter().add(new SecondaryButton(getTranslation("cancel_button"), ignoredEvent -> close()), saveButton);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showDialog(Object item, SerializableConsumer<?> callback) {
        this.onSaveCallback = (SerializableConsumer<T>) callback;
        binder.readBean(beanType.cast(item));
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        tabs.setSelectedTab(tab1);
        open();
    }

    /**
     * Обработчик сохранения данных при нажатии кнопки "Сохранить".
     */
    private void onSave() {
        if (binder.validate().isOk()) {
            if (onSaveCallback != null) {
                try {
                    T user = beanType.getDeclaredConstructor().newInstance();
                    binder.writeBeanIfValid(user);
                    onSaveCallback.accept(user);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            close();
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
