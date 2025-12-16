package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сущность шаблона письма/сообщения (mail template).
 * <p>
 * Хранит шаблоны электронных писем и сообщений, используемые системой рассылок.
 * Шаблоны содержат структурированный контент для массовых уведомлений и могут быть
 * переиспользованы в различных рассылках.
 * <p>
 * Основные компоненты:
 * - name: название шаблона (используется для идентификации и поиска)
 * - subject: тема письма/сообщения
 * - bodyHtml: HTML-версия тела письма (для email)
 * - bodyText: текстовая версия тела письма (для SMS, plain text email)
 * - attachments: JSON-объект (jsonb) с информацией о вложениях
 * Например: {"files": [{"name": "file.pdf", "url": "..."}, ...]}
 * <p>
 * Связи:
 * - createdBy: пользователь, создавший шаблон (ManyToOne, LAZY)
 * - mailings: рассылки, использующие этот шаблон (OneToMany, LAZY)
 * <p>
 * Аудит:
 * - createdAt: автоматически устанавливается при создании (неизменяемое)
 * - updatedAt: автоматически обновляется при изменении шаблона
 * <p>
 * Примечание: это сущность конфигурации/контента. Шаблоны редактируемы,
 * поэтому updated_at обновляется при каждом изменении.
 * Не каскадирует удаление на связанные сущности (User, Mailing).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mail_templates", indexes = {
        @Index(name = "idx_mail_templates_created_by", columnList = "created_by"),
        @Index(name = "idx_mail_templates_name", columnList = "name")
})
public class MailTemplate {
    /**
     * Уникальный идентификатор шаблона.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название шаблона.
     * <p>
     * Используется для идентификации и поиска шаблонов.
     * Должно быть уникальным в рамках системы (рекомендуется).
     */
    @Column(nullable = false)
    private String name;

    /**
     * Тема письма/сообщения.
     * <p>
     * Отображается в заголовке email или как заголовок сообщения.
     * Может содержать переменные для подстановки (например, {{userName}}).
     */
    @Column(nullable = false)
    private String subject;

    /**
     * HTML-версия тела письма.
     * <p>
     * Используется для email-рассылок с форматированием.
     * Может содержать HTML-теги и переменные для подстановки.
     */
    @Column(name = "body_html", columnDefinition = "TEXT")
    private String bodyHtml;

    /**
     * Текстовая версия тела письма.
     * <p>
     * Используется для SMS, plain text email или как fallback для HTML.
     * Не содержит форматирования, только текст.
     */
    @Column(name = "body_text", columnDefinition = "TEXT")
    private String bodyText;

    /**
     * Вложения к письму/сообщению.
     * <p>
     * JSON-объект (jsonb) с информацией о файлах-вложениях.
     * Формат: {"files": [{"name": "file.pdf", "url": "...", "size": 12345}, ...]}
     * или Map<String, Object> для программного доступа.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attachments;

    /**
     * Пользователь, создавший шаблон.
     * <p>
     * Связь с таблицей users (users.id).
     * Не каскадирует удаление - удаление пользователя не удаляет шаблоны.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /**
     * Рассылки, использующие этот шаблон.
     * <p>
     * Список рассылок (Mailing), которые ссылаются на этот шаблон.
     * Один шаблон может быть использован в множестве рассылок.
     * Не каскадирует удаление - удаление шаблона не удаляет рассылки.
     */
    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Mailing> mailings = new ArrayList<>();

    /**
     * Дата и время создания шаблона.
     * <p>
     * Автоматически устанавливается при создании записи.
     * Неизменяемое поле (immutable).
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время последнего обновления шаблона.
     * <p>
     * Автоматически обновляется при каждом изменении записи.
     * Шаблоны редактируемы, поэтому это поле обновляется при каждом изменении.
     */
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    /**
     * Шаблоны считаются равными, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailTemplate that = (MailTemplate) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

