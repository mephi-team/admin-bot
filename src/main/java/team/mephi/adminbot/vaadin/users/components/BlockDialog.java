package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.SimpleDialog;

public class BlockDialog extends Dialog implements SimpleDialog<SimpleUser> {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());
    private final Tabs tabs = new Tabs();
    private final Tab tab1 = new Tab(getTranslation("dialog_user_block_tab_warning_label"));
    private final Tab tab2 = new Tab(getTranslation("dialog_user_block_tab_block_label"));

    private SerializableConsumer<SimpleUser> onSaveCallback;

    public BlockDialog() {
        var form = new BlockUserInfo();
        var form1 = new WarningForm();
        var form2 = new BlockForm();
        var form3 = new BlockUserMessage();
        form2.setVisible(false);
        tabs.add(tab1, tab2);
        tabs.addSelectedChangeListener(e -> {
           if (e.getSelectedTab().equals(tab1)){
               form1.setVisible(true);
               form2.setVisible(false);
           } else {
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
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);
    }

    @Override
    public void showDialog(Object item, SerializableConsumer<SimpleUser> callback) {
        this.onSaveCallback = callback;
        binder.readBean((SimpleUser) item);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        tabs.setSelectedTab(tab1);
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
