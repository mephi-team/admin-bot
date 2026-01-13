package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.MailingTaskStatus;

import java.time.Instant;

/**
 * Сущность задачи рассылки (mailing task).
 * <p>
 * Хранит метаданные планирования и выполнения задач рассылки.
 * Задача представляет собой запланированное или выполняемое действие по отправке рассылки.
 * <p>
 * Основные компоненты:
 * - sendAt: следующее время выполнения задачи
 * - repeatCron: опциональное cron-выражение для повторяющихся задач
 * - repeatUntil: опциональная дата окончания повторений
 * - status: текущий статус задачи (SCHEDULED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED)
 * - executedAt: время последнего выполнения задачи
 * <p>
 * Связи:
 * - mailing: рассылка, к которой относится задача (ManyToOne, LAZY)
 * <p>
 * Семантика планирования:
 * - send_at: следующее время выполнения задачи
 * - repeat_cron: опциональное cron-выражение для периодических задач
 * - repeat_until: опциональная дата окончания повторений
 * - executed_at: время последнего выполнения задачи
 * <p>
 * Примечание: это сущность метаданных планировщика, не выполняет отправку напрямую.
 * Не каскадирует удаление на связанную рассылку (Mailing).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mailing_task", indexes = {
        @Index(name = "idx_mailing_task_mailing_id", columnList = "mailing_id"),
        @Index(name = "idx_mailing_task_status", columnList = "status"),
        @Index(name = "idx_mailing_task_send_at", columnList = "send_at")
})
public class MailingTask {
    /**
     * Уникальный идентификатор задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Рассылка, к которой относится задача.
     * <p>
     * Связь с таблицей mailings (mailings.id).
     * Загрузка ленивая (LAZY) для оптимизации производительности.
     * Не каскадирует удаление - удаление задачи не удаляет рассылку.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailing_id", nullable = false)
    private Mailing mailing;

    /**
     * Следующее время выполнения задачи.
     * <p>
     * Определяет, когда задача должна быть выполнена в следующий раз.
     * Используется планировщиком для определения задач, готовых к выполнению.
     */
    @Column(name = "send_at")
    private Instant sendAt;

    /**
     * Cron-выражение для повторяющихся задач.
     * <p>
     * Опциональное поле. Если задано, задача будет выполняться периодически
     * согласно cron-выражению до даты repeatUntil (если она задана).
     * Пример: "0 0 * * *" - каждый день в полночь.
     */
    @Column(name = "repeat_cron")
    private String repeatCron;

    /**
     * Дата окончания повторений.
     * <p>
     * Опциональное поле. Если задано, повторяющаяся задача (с repeatCron)
     * не будет выполняться после этой даты.
     */
    @Column(name = "repeat_until")
    private Instant repeatUntil;

    /**
     * Текущий статус задачи.
     *
     * @see MailingTaskStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingTaskStatus status;

    /**
     * Время последнего выполнения задачи.
     * <p>
     * Обновляется после каждого выполнения задачи.
     * Может быть null, если задача еще не выполнялась.
     */
    @Column(name = "executed_at")
    private Instant executedAt;

    /**
     * Задачи считаются равными, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailingTask that = (MailingTask) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Хеш-код основан на классе для обеспечения консистентности с equals().
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
