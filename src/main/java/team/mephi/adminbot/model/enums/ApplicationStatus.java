package team.mephi.adminbot.model.enums;

/**
 * Статус заявки / формы записи на обучение.
 * <p>
 * Показывает текущее состояние обработки заявки:
 * - NEW: новая заявка, ожидает обработки
 * - IN_PROGRESS: заявка находится в процессе обработки
 * - APPROVED: заявка одобрена
 * - REJECTED: заявка отклонена
 */
public enum ApplicationStatus {
    NEW,            // Новая заявка
    IN_PROGRESS,    // В процессе обработки
    APPROVED,       // Одобрена
    REJECTED        // Отклонена
}

