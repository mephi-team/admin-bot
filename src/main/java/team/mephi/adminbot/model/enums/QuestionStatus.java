package team.mephi.adminbot.model.enums;

/**
 * Статус вопроса пользователя.
 *
 * <p>Жизненный цикл вопроса:
 * <ul>
 *   <li>NEW - вопрос создан, ожидает назначения эксперта</li>
 *   <li>IN_PROGRESS - вопрос назначен эксперту, находится в обработке</li>
 *   <li>ANSWERED - на вопрос дан ответ</li>
 * </ul>
 */
public enum QuestionStatus {
    NEW,
    IN_PROGRESS,
    ANSWERED
}

