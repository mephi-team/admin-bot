package team.mephi.adminbot.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.NotificationStatus;
import team.mephi.adminbot.model.enums.NotificationType;

import java.time.Instant;

/**
 * Сущность уведомления в очереди отправки.
 * <p>
 * Эта таблица работает как очередь (outbox):
 * бизнес-логика кладёт сюда уведомления,
 * а фоновые воркеры потом их отправляют.
 * <p>
 * Жизненный цикл статуса:
 * - PENDING    — уведомление только что добавлено в очередь
 * - SENT       — уведомление успешно отправлено
 * - FAILED     — при отправке произошла ошибка
 * - DELIVERED  — уведомление доставлено получателю
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications_queue")
public class NotificationQueue {

    /**
     * Технический первичный ключ записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип уведомления.
     * <p>
     * Например: EMAIL, TELEGRAM и т.д.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    /**
     * Получатель уведомления.
     * <p>
     * Это может быть email, chatId, userId —
     * в зависимости от типа уведомления.
     */
    @Column(name = "recipient", nullable = false)
    private String recipient;

    /**
     * Данные уведомления в формате JSON.
     * <p>
     * Содержит всё, что нужно для отправки:
     * текст, параметры шаблона, метаданные и т.п.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode payload;

    /**
     * Текущий статус уведомления.
     * <p>
     * См. описание жизненного цикла выше.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    /**
     * Текст ошибки, если отправка завершилась неудачей.
     * <p>
     * Заполняется только при статусе FAILED.
     */
    @Column(name = "error")
    private String error;

    /**
     * Дата и время создания записи в очереди.
     * <p>
     * Проставляется автоматически
     * и после этого не меняется.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время успешной отправки уведомления.
     * <p>
     * Заполняется после перехода в статус SENT.
     */
    @Column(name = "sent_at")
    private Instant sentAt;

    /**
     * Хук JPA, который срабатывает перед сохранением записи.
     * <p>
     * Если значения не заданы:
     * - устанавливает статус PENDING
     */
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = NotificationStatus.PENDING;
        }
    }
}
