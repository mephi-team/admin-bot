package team.mephi.adminbot.model.enums;

/**
 * Статус доставки и прочтения сообщения.
 *
 * <p>Определяет текущее состояние сообщения в системе доставки:
 * <ul>
 *   <li>SENT - сообщение отправлено, но еще не доставлено</li>
 *   <li>DELIVERED - сообщение доставлено получателю</li>
 *   <li>READ - сообщение прочитано получателем</li>
 *   <li>FAILED - ошибка при отправке/доставке сообщения</li>
 * </ul>
 *
 * <p>Жизненный цикл статуса:
 * <ol>
 *   <li>При создании: SENT</li>
 *   <li>После доставки: DELIVERED</li>
 *   <li>После прочтения: READ</li>
 *   <li>При ошибке: FAILED (с заполненным statusReason)</li>
 * </ol>
 */
public enum MessageStatus {
    SENT,
    DELIVERED,
    READ,
    FAILED
}

