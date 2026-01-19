package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.vaadin.components.RightDrawer;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.core.DialogWithTitle;

/**
 * Диалоговое окно для блокировки пользователя с информацией и предупреждением.
 *
 * @param <T> тип объекта, связанного с блокировкой
 */
public class BlockDialog<T> extends RightDrawer implements DialogWithTitle {
    private final BeanValidationBinder<T> binder;
    private final Tabs tabs = new Tabs();
    private final Tab tab1 = new Tab(getTranslation("dialog_user_block_tab_warning_label"));
    private final Tab tab2 = new Tab(getTranslation("dialog_user_block_tab_block_label"));
    private final Class<T> beanType;
    private SerializableConsumer<T> onSaveCallback;
    private T item;
    private final Button blockButton = new PrimaryButton(getTranslation("block_button"), ignoredEvent -> onSave());
    private final Button warningButton = new PrimaryButton(getTranslation("warning_button"), ignoredEvent -> onSave());

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

        var warningReason = binder.forField(form1.getWarningReason())
                .asRequired()
                .bind("warningReason");
        var blockReason = binder.forField(form2.getBlockReason())
                .asRequired()
                .bind("blockReason");
        blockReason.setValidatorsDisabled(true);
        binder.forField(form3.getMessage()).bind("messageForUser");

        form2.setVisible(false);
        tabs.add(tab1, tab2);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab().equals(tab1)) {
                form1.setVisible(true);
                form2.setVisible(false);
                warningButton.setVisible(true);
                blockButton.setVisible(false);
                blockReason.setValidatorsDisabled(true);
            } else if (e.getSelectedTab().equals(tab2)) {
                form1.setVisible(false);
                form2.setVisible(true);
                warningButton.setVisible(false);
                blockButton.setVisible(true);
                warningReason.setValidatorsDisabled(true);
            }
        });
        binder.bindInstanceFields(form);
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);

        setHeaderTitle("dialog_user_block_title");
        add(tabs, form, form1, form2, form3);
        getFooter().add(blockButton, warningButton);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showDialog(Object item, SerializableConsumer<?> callback) {
        this.onSaveCallback = (SerializableConsumer<T>) callback;
        this.item = beanType.cast(item);
        binder.readBean(beanType.cast(item));
        blockButton.setVisible(false);
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
                    binder.writeBeanIfValid(this.item);
                    onSaveCallback.accept(this.item);
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
