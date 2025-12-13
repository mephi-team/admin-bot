package team.mephi.adminbot.model.enums;

/**
 * Канал доставки сообщения получателю рассылки.
 *
 * Определяет способ доставки сообщения:
 * - EMAIL: доставка по электронной почте
 * - TELEGRAM: доставка через Telegram
 * - SMS: доставка через SMS
 */
public enum MailingChannel {
    EMAIL,
    TELEGRAM,
    SMS
}

