package team.mephi.adminbot.model.enums;

/**
 * Статус ответа на вопрос пользователя.
 *
 * <p>Жизненный цикл ответа:
 * <ul>
 *   <li>DRAFT - ответ создан, но ещё не отправлен</li>
 *   <li>SENT - ответ отправлен пользователю</li>
 *   <li>UPDATED - ответ был обновлён после отправки</li>
 * </ul>
 */
public enum AnswerStatus {
    DRAFT,
    SENT,
    UPDATED
}

