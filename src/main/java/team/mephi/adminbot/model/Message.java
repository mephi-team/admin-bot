package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.MessageSenderType;
import team.mephi.adminbot.model.enums.MessageStatus;

import java.time.Instant;

/**
 * Сущность сообщения в диалоге.
 *
 * <p>Хранит полную историю сообщений в диалогах, включая:
 * <ul>
 *   <li>Текст сообщения и вложения (JSON)</li>
 *   <li>Статус доставки и прочтения</li>
 *   <li>Ссылки на внешние платформы (например, Telegram)</li>
 *   <li>Информацию об отправителе (полиморфная связь)</li>
 * </ul>
 *
 * <p>Полиморфизм отправителя:
 * <ul>
 *   <li>senderType определяет тип отправителя (USER, ADMIN, BOT, EXPERT)</li>
 *   <li>sender ссылается на users.id для USER, ADMIN, EXPERT</li>
 *   <li>sender может быть null для BOT (системные сообщения)</li>
 * </ul>
 *
 * <p>Жизненный цикл:
 * <ul>
 *   <li>Сообщения создаются как append-only (только добавление)</li>
 *   <li>Обновляются только статусы доставки/прочтения</li>
 *   <li>При создании сообщения обновляются dialogs.last_message_at и dialogs.unread_count</li>
 * </ul>
 *
 * <p>Важно:
 * <ul>
 *   <li>Не каскадирует удаление на Dialog или User</li>
 *   <li>Все связи загружаются лениво (LAZY) для оптимизации</li>
 *   <li>Текст может быть null, если есть вложения</li>
 * </ul>
 */
@Data
@Entity
@Table(name = "dialog_messages", indexes = {
        @Index(name = "idx_dialog_created_at", columnList = "dialog_id,created_at")
})
public class Message {
    /**
     * Внутренний ID сообщения в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Диалог, к которому относится сообщение.
     * <p>
     * Связь многие-к-одному с таблицей dialogs.
     * Загружается лениво для оптимизации производительности.
     * Не каскадирует удаление.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;

    /**
     * Тип отправителя сообщения.
     * <p>
     * Определяет, кто отправил сообщение: пользователь, администратор, бот или эксперт.
     * Используется для полиморфной связи с отправителем.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private MessageSenderType senderType;

    /**
     * Отправитель сообщения (пользователь).
     * <p>
     * Ссылается на users.id для типов USER, ADMIN, EXPERT.
     * Может быть null для BOT (системные сообщения).
     * Загружается лениво для оптимизации производительности.
     * Не каскадирует удаление.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * Текст сообщения.
     * <p>
     * Может быть null, если сообщение содержит только вложения.
     */
    @Column(columnDefinition = "TEXT")
    private String text;

    /**
     * Вложения к сообщению в формате JSON.
     * <p>
     * Хранится как PostgreSQL jsonb.
     * Может содержать файлы, изображения, ссылки и другие метаданные.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object attachments;

    /**
     * Статус доставки и прочтения сообщения.
     * <p>
     * Определяет текущее состояние сообщения: отправлено, доставлено, прочитано или ошибка.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    /**
     * Причина статуса (например, описание ошибки при FAILED).
     * <p>
     * Заполняется при возникновении ошибок или особых ситуаций.
     */
    @Column(name = "status_reason")
    private String statusReason;

    /**
     * ID сообщения в Telegram (если сообщение синхронизировано с Telegram).
     * <p>
     * Используется для связи с внешними платформами.
     */
    @Column(name = "telegram_message_id")
    private String telegramMessageId;

    /**
     * Дата и время создания сообщения.
     * <p>
     * Устанавливается автоматически при создании записи.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
//    @CreationTimestamp // Мешает созданию тестовых сообщений прошлой датой
    private Instant createdAt;

    /**
     * Дата и время последнего обновления сообщения.
     * <p>
     * Обновляется автоматически при изменении записи.
     * Обычно обновляется только при изменении статуса.
     */
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    // ===== equals() и hashCode() =====

    /**
     * Сообщения считаются одинаковыми, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id != null && id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
