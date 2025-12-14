package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.MailingChannel;
import team.mephi.adminbot.model.enums.MailingRecipientStatus;

import java.time.Instant;

/**
 * Сущность получателя рассылки (mailing recipient).
 *
 * Хранит информацию о доставке и статусе прочтения сообщения конкретному получателю.
 * Отслеживает жизненный цикл доставки: от создания записи до прочтения сообщения получателем.
 *
 * Основные компоненты:
 * - mailing: рассылка, к которой относится получатель (ManyToOne, LAZY)
 * - user: пользователь-получатель (ManyToOne, LAZY)
 * - channel: канал доставки (EMAIL, TELEGRAM, SMS)
 * - status: текущий статус доставки (PENDING, SENT, DELIVERED, READ, FAILED)
 * - statusReason: опциональная причина статуса (например, причина ошибки)
 * - messageId: идентификатор сообщения в системе доставки
 * - sentAt: время отправки сообщения
 * - readAt: время прочтения сообщения получателем
 *
 * Связи:
 * - mailing: рассылка, к которой относится получатель (ManyToOne, LAZY)
 * - user: пользователь-получатель (ManyToOne, LAZY)
 *
 * Ограничения:
 * - (mailing_id, user_id) должны быть уникальными для каждой рассылки
 * - channel и status обязательны (NOT NULL)
 * - sent_at и read_at опциональны (NULLABLE)
 *
 * Примечание: не каскадирует удаление на связанные сущности (Mailing, User).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "mailing_recipients",
        indexes = {
                @Index(name = "idx_mailing_user", columnList = "mailing_id, user_id"),
                @Index(name = "idx_mailing_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mailing_recipient_mailing_user", columnNames = {"mailing_id", "user_id"})
        }
)
public class MailingRecipient {
    /**
     * Уникальный идентификатор получателя рассылки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Рассылка, к которой относится получатель.
     *
     * Связь с таблицей mailings (mailings.id).
     * Не каскадирует удаление - удаление рассылки не удаляет получателей.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailing_id", nullable = false)
    private Mailing mailing;

    /**
     * Пользователь-получатель рассылки.
     *
     * Связь с таблицей users (users.id).
     * Не каскадирует удаление - удаление пользователя не удаляет записи получателей.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Канал доставки сообщения.
     *
     * Определяет способ доставки: EMAIL, TELEGRAM, SMS.
     * Обязательное поле.
     *
     * @see MailingChannel
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingChannel channel;

    /**
     * Статус доставки сообщения получателю.
     *
     * Отслеживает жизненный цикл доставки: PENDING → SENT → DELIVERED → READ.
     * В случае ошибки статус меняется на FAILED.
     * Обязательное поле.
     *
     * @see MailingRecipientStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingRecipientStatus status;

    /**
     * Причина статуса доставки.
     *
     * Опциональное поле, содержащее дополнительную информацию о статусе.
     * Например, причина ошибки при FAILED или дополнительная информация о доставке.
     */
    @Column(name = "status_reason")
    private String statusReason;

    /**
     * Идентификатор сообщения в системе доставки.
     *
     * Опциональное поле, содержащее идентификатор сообщения,
     * возвращаемый системой доставки (например, ID письма в почтовом сервере).
     */
    @Column(name = "message_id")
    private String messageId;

    /**
     * Время отправки сообщения получателю.
     *
     * Опциональное поле, устанавливается при успешной отправке сообщения.
     * Используется для отслеживания времени доставки.
     */
    @Column(name = "sent_at")
    private Instant sentAt;

    /**
     * Время прочтения сообщения получателем.
     *
     * Опциональное поле, устанавливается при прочтении сообщения получателем.
     * Используется для отслеживания вовлеченности получателей.
     */
    @Column(name = "read_at")
    private Instant readAt;

    /**
     * Получатели рассылки считаются равными, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailingRecipient that = (MailingRecipient) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Хеш-код основан на ID получателя.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
