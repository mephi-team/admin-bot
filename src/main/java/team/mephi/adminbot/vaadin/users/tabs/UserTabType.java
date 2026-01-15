package team.mephi.adminbot.vaadin.users.tabs;

import lombok.Getter;

/**
 * Типы вкладок пользователей.
 */
public enum UserTabType {
    VISITOR,
    CANDIDATE,
    MIDDLE_CANDIDATE,
    STUDENT,
    FREE_LISTENER,
    LC_EXPERT,
    TUTOR;

    /**
     * Ключ для локализации метки вкладки.
     */
    @Getter
    private final String tabLabelKey = "page_users_tab_" + name().toLowerCase() + "_label";
}
