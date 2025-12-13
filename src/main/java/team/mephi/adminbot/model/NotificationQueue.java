package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Сущность уведомления в очереди отправки.
 *
 * Эта таблица работает как очередь (outbox):
 * бизнес-логика кладёт сюда уведомления,
 * а фоновые воркеры потом их отправляют.
 *
 * Жизненный цикл статуса:
 * - NEW        — уведомление только что добавлено в очередь
 * - PROCESSING — уведомление сейчас отправляется
 * - SENT       — уведомление успешно отправлено
 * - FAILED     — при отправке произошла ошибка
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
     *
     * Например: EMAIL, TELEGRAM, PUSH и т.д.
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * Получатель уведомления.
     *
     * Это может быть email, chatId, userId —
     * в зависимости от типа уведомления.
     */
    @Column(name = "recipient", nullable = false)
    private String recipient;

    /**
     * Данные уведомления в формате JSON.
     *
     * Содержит всё, что нужно для отправки:
     * текст, параметры шаблона, метаданные и т.п.
     */
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    /**
     * Текущий статус уведомления.
     *
     * См. описание жизненного цикла выше.
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Текст ошибки, если отправка завершилась неудачей.
     *
     * Заполняется только при статусе FAILED.
     */
    @Column(name = "error")
    private String error;

    /**
     * Дата и время создания записи в очереди.
     *
     * Проставляется автоматически
     * и после этого не меняется.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата и время успешной отправки уведомления.
     *
     * Заполняется после перехода в статус SENT.
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    /**
     * Хук JPA, который срабатывает перед сохранением записи.
     *
     * Если значения не заданы:
     * - ставит текущее время в createdAt
     * - устанавливает статус NEW
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "NEW";
        }
    }
}
