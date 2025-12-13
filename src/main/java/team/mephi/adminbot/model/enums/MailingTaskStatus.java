package team.mephi.adminbot.model.enums;

/**
 * Статус задачи рассылки (mailing task).
 *
 * Определяет текущее состояние задачи выполнения рассылки:
 * - SCHEDULED: задача запланирована и ожидает выполнения
 * - IN_PROGRESS: задача выполняется в данный момент
 * - COMPLETED: задача успешно завершена
 * - FAILED: выполнение задачи завершилось с ошибкой
 * - CANCELLED: задача отменена и не будет выполнена
 */
public enum MailingTaskStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED
}

