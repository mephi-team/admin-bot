package team.mephi.adminbot.model.enums;

/**
 * Статус рассылки (mailing).
 * <p>
 * Определяет текущее состояние рассылки:
 * - DRAFT: черновик, рассылка создана, но не активирована
 * - ACTIVE: активная рассылка, выполняется в данный момент
 * - PAUSED: рассылка приостановлена (можно возобновить)
 * - FINISHED: рассылка завершена успешно
 */
public enum MailingStatus {
    DRAFT,
    ACTIVE,
    PAUSED,
    FINISHED,
    ERROR,
    CANCELED
}

