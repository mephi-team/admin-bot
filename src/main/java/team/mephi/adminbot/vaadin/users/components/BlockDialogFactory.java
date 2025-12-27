package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class BlockDialogFactory {
    public BlockDialogFactory() {

    }

    public BlockDialog create() {
        return new BlockDialog();
    }
}
