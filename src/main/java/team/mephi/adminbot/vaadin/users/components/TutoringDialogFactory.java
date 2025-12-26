package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class TutoringDialogFactory {
    public TutoringDialogFactory() {

    }

    public TutoringDialog create() {
        return new TutoringDialog();
    }
}
