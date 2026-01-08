package team.mephi.adminbot.vaadin.service;

import lombok.Getter;

public enum DialogType {
    USERS_CREATED,
    USERS_VIEW,
    USERS_EDIT,
    USERS_BLOCKED,
    TUTORS_BLOCKED,
    TUTORS_VIEW,
    TUTORS_UPDATED,
    TUTORS_EDIT,
    SENT_CREATED,
    TEMPLATES_CREATED,
    DRAFT_CREATED,
    MAILING_SAVED,
    TEMPLATE_SAVED,
    ANSWER_SEND,
    DELETE_USERS,
    DELETE_QUESTION,
    DELETE_QUESTION_ALL,
    ACCEPT_USERS,
    ACCEPT_USERS_ALL,
    REJECT_USERS,
    REJECT_USERS_ALL,
    DELETE_MAILING,
    DELETE_MAILING_ALL,
    RETRY_MAILING,
    CANCEL_MAILING,
    DELETE_TEMPLATE,
    DELETE_TEMPLATE_ALL,
    EXPEL_USERS;

    @Getter
    private final String dialogTitleKey;
    @Getter
    private final String notificationKey;

    DialogType() {
        this.dialogTitleKey = "dialog_" + name().toLowerCase() + "_title";
        this.notificationKey = "notification_" + name().toLowerCase();
    }
}
