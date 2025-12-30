package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleUser;

public class BlockDialog extends Dialog {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());
    private final TabSheet tabSheet = new TabSheet();

    private SerializableConsumer<SimpleUser> onSaveCallback;

    public BlockDialog() {
        var form1 = new WarningForm();
        var form2 = new BlockForm();
        tabSheet.add(getTranslation("dialog_user_block_tab_warning_label"), form1);
        tabSheet.add(getTranslation("dialog_user_block_tab_block_label"), form2);
        binder.bindInstanceFields(form1);
        binder.bindInstanceFields(form2);
        setHeaderTitle("dialog_user_block_title");
        add(tabSheet);
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);
    }

    public void openForView(SimpleUser user, SerializableConsumer<SimpleUser> callback) {
        this.onSaveCallback = callback;
        binder.readBean(user);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                SimpleUser user = new SimpleUser();
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
