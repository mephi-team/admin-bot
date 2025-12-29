package team.mephi.adminbot.vaadin.users.actions;

import java.util.List;

public interface StudentActions extends UserActions {
    void onExpel(List<Long> ids);
}
