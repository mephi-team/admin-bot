package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import team.mephi.adminbot.model.enums.ApplicationStatus;

import java.time.Instant;

/**
 * Сущность заявки / формы записи на обучение.
 * <p>
 * Хранит данные заявок на поступление, включая:
 * - личные данные кандидата (имя, фамилия, email, телефон)
 * - выбранное направление обучения
 * - статус обработки заявки
 * - источник заявки и UTM-метки для аналитики
 * <p>
 * Используется для:
 * - антидубликации заявок (по email, phone, first_name + last_name)
 * - аналитики источников трафика (UTM-метки)
 * - отслеживания статуса обработки заявок
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications", indexes = {
        @Index(name = "idx_applications_email", columnList = "email"),
        @Index(name = "idx_applications_phone", columnList = "phone"),
        @Index(name = "idx_applications_first_name_last_name", columnList = "first_name,last_name"),
        @Index(name = "idx_applications_direction_id", columnList = "direction_id"),
        @Index(name = "idx_applications_status", columnList = "status"),
        @Index(name = "idx_applications_user_id", columnList = "user_id")
})
public class Application {

    /**
     * Внутренний ID заявки в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, связанный с заявкой.
     * <p>
     * Связь с таблицей users.
     * Может быть null, если заявка создана до регистрации пользователя.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Имя кандидата.
     * <p>
     * Используется для антидубликации вместе с last_name.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Фамилия кандидата.
     * <p>
     * Используется для антидубликации вместе с first_name.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Email кандидата.
     * <p>
     * Используется для антидубликации заявок.
     * Индексируется для быстрого поиска дубликатов.
     */
    @Column(name = "email")
    private String email;

    /**
     * Номер телефона кандидата.
     * <p>
     * Используется для антидубликации заявок.
     * Индексируется для быстрого поиска дубликатов.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Направление обучения, на которое подана заявка.
     * <p>
     * Связь с таблицей directions.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    private Direction direction;

    /**
     * Статус обработки заявки.
     * <p>
     * Показывает текущее состояние заявки:
     * NEW, IN_PROGRESS, APPROVED, REJECTED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;

    /**
     * Источник заявки.
     * <p>
     * Откуда пришла заявка (например: "website", "telegram", "referral").
     */
    @Column(name = "source")
    private String source;

    /**
     * UTM-метка: источник трафика.
     * <p>
     * Используется для аналитики источников заявок.
     * Пример: "google", "yandex", "facebook".
     */
    @Column(name = "utm_source")
    private String utmSource;

    /**
     * UTM-метка: тип трафика.
     * <p>
     * Используется для аналитики источников заявок.
     * Пример: "cpc", "organic", "email".
     */
    @Column(name = "utm_medium")
    private String utmMedium;

    /**
     * UTM-метка: название кампании.
     * <p>
     * Используется для аналитики источников заявок.
     * Пример: "spring_2024", "summer_promo".
     */
    @Column(name = "utm_campaign")
    private String utmCampaign;

    /**
     * UTM-метка: ключевое слово.
     * <p>
     * Используется для аналитики источников заявок.
     * Обычно используется в контекстной рекламе.
     */
    @Column(name = "utm_term")
    private String utmTerm;

    /**
     * UTM-метка: дополнительный параметр.
     * <p>
     * Используется для аналитики источников заявок.
     * Позволяет различать варианты одной кампании.
     */
    @Column(name = "utm_content")
    private String utmContent;

    /**
     * Дата и время создания заявки.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время последнего обновления заявки.
     */
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    /**
     * Инициализация дат при создании записи.
     */
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    /**
     * Обновление даты изменения записи.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Заявки считаются равными,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

