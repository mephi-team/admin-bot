package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Сущность рассылки (mailing).
 *
 * Хранит метаданные массовых уведомлений, кампаний и автоматизированных сообщений.
 * Рассылка представляет собой конфигурацию для отправки сообщений множеству получателей
 * через различные каналы связи (email, Telegram, SMS и т.д.).
 *
 * Основные компоненты:
 * - Каналы связи (channels): JSON-объект с настройками каналов доставки
 * - Фильтры получателей (filters): JSON-объект с критериями отбора получателей
 * - Шаблон сообщения (template): ссылка на шаблон письма/сообщения
 * - Статус (status): текущее состояние рассылки (DRAFT, ACTIVE, PAUSED, FINISHED)
 * - Причина/код (reasonCode): JSON-объект с дополнительной информацией о статусе
 *
 * Связи:
 * - createdBy: пользователь, создавший рассылки
 * - template: шаблон сообщения (опционально)
 * - tasks: задачи выполнения рассылки (MailingTask)
 * - recipients: получатели рассылки (MailingRecipient)
 *
 * Примечание: это сущность конфигурации/метаданных, не выполняет отправку напрямую.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mailings", indexes = {
        @Index(name = "idx_mailings_status", columnList = "status"),
        @Index(name = "idx_mailings_created_by", columnList = "created_by"),
        @Index(name = "idx_mailings_template_id", columnList = "template_id")
})
public class Mailing {
    /**
     * Уникальный идентификатор рассылки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название рассылки.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Описание рассылки.
     *
     * Может содержать информацию о цели рассылки, целевой аудитории и т.д.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Каналы связи для доставки сообщений.
     *
     * JSON-объект (jsonb) с настройками каналов:
     * например, {"email": true, "telegram": true, "sms": false}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> channels;

    /**
     * Фильтры для отбора получателей рассылки.
     *
     * JSON-объект (jsonb) с критериями фильтрации:
     * например, {"role": "STUDENT", "direction_id": 123, "status": "ACTIVE"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> filters;

    /**
     * Текущий статус рассылки.
     *
     * @see MailingStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingStatus status;

    /**
     * Пользователь, создавший рассылки.
     *
     * Связь с таблицей users (users.id).
     * Не каскадирует удаление - удаление пользователя не удаляет рассылки.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Дата и время создания рассылки.
     *
     * Автоматически устанавливается при создании записи.
     * Неизменяемое поле (immutable).
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Шаблон сообщения для рассылки.
     *
     * Связь с таблицей mail_templates (mail_templates.id).
     * Опциональное поле - рассылка может не иметь шаблона.
     * Не каскадирует удаление - удаление шаблона не удаляет рассылки.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private MailTemplate template;

    /**
     * Код причины или дополнительная информация о статусе.
     *
     * JSON-объект (jsonb) с метаданными:
     * например, {"reason": "MANUAL_PAUSE", "paused_by": 123, "paused_at": "2024-01-01T00:00:00Z"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reason_code", columnDefinition = "jsonb")
    private Map<String, Object> reasonCode;

    /**
     * Задачи выполнения рассылки.
     *
     * Список задач (MailingTask), связанных с этой рассылкой.
     * Каскадирует операции на дочерние задачи.
     */
    @OneToMany(mappedBy = "mailing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MailingTask> tasks;

    /**
     * Получатели рассылки.
     *
     * Список получателей (MailingRecipient), связанных с этой рассылкой.
     * Каскадирует операции на дочерние записи получателей.
     */
    @OneToMany(mappedBy = "mailing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MailingRecipient> recipients;

    /**
     * Рассылки считаются равными, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mailing mailing = (Mailing) o;
        return id != null && id.equals(mailing.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

