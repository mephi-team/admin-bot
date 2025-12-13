package team.mephi.adminbot.model.enums;

/**
 * Тип отправителя сообщения в диалоге.
 *
 * <p>Определяет, кто отправил сообщение:
 * <ul>
 *   <li>USER - обычный пользователь (студент/кандидат)</li>
 *   <li>ADMIN - администратор системы</li>
 *   <li>BOT - автоматический бот (системные сообщения)</li>
 *   <li>EXPERT - эксперт/тьютор</li>
 * </ul>
 *
 * <p>Полиморфизм отправителя:
 * <ul>
 *   <li>Для USER, ADMIN, EXPERT - поле sender_id ссылается на users.id</li>
 *   <li>Для BOT - sender_id может быть null (системные сообщения)</li>
 * </ul>
 */
public enum MessageSenderType {
    USER,
    ADMIN,
    BOT,
    EXPERT
}

