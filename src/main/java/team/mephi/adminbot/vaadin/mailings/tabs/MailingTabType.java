package team.mephi.adminbot.vaadin.mailings.tabs;

import lombok.Getter;

public enum MailingTabType {
    SENT,
    TEMPLATES,
    DRAFT;

    @Getter
    private final String tabLabelKey = "page_mailing_tab_" + name().toLowerCase() + "_label";
}
