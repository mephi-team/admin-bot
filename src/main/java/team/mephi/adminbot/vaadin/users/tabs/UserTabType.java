package team.mephi.adminbot.vaadin.users.tabs;

import lombok.Getter;

public enum UserTabType {
    VISITOR,
    CANDIDATE,
    MIDDLE_CANDIDATE,
    STUDENT,
    FREE_LISTENER,
    LC_EXPERT,
    TUTOR;

    @Getter
    private final String tabLabelKey = "page_users_tab_" + name().toLowerCase() + "_label";
}
