package team.mephi.adminbot.model.enums;

/**
 * Статус формы записи на обучение.
 *
 * <p>Жизненный цикл формы записи:
 * <ul>
 *   <li>PENDING - форма создана, ожидает отправки пользователю</li>
 *   <li>SENT - форма отправлена пользователю</li>
 *   <li>USED - форма использована пользователем для записи на обучение</li>
 *   <li>EXPIRED - срок действия формы истёк</li>
 *   <li>FAILED - произошла ошибка при обработке формы</li>
 * </ul>
 */
public enum EnrollmentStatus {
    PENDING,
    SENT,
    USED,
    EXPIRED,
    FAILED
}

