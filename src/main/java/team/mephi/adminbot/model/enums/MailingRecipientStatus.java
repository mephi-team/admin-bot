package team.mephi.adminbot.model.enums;

/**
 * Статус доставки сообщения получателю рассылки.
 *
 * Отслеживает жизненный цикл доставки сообщения:
 * - PENDING: сообщение ожидает отправки
 * - SENT: сообщение отправлено
 * - DELIVERED: сообщение доставлено получателю
 * - READ: сообщение прочитано получателем
 * - FAILED: отправка не удалась
 */
public enum MailingRecipientStatus {
    PENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

