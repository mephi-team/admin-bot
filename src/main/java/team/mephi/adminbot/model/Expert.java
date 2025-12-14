package team.mephi.adminbot.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность эксперта системы.
 *
 * <p>Хранит эксперт-специфичные настройки для пользователей с ролью lc_expert.
 * Каждый эксперт всегда связан с пользователем (1-to-1 relationship с shared primary key).
 *
 * <p>Основные компоненты:
 * <ul>
 *   <li>user: связанный пользователь (shared primary key через @MapsId)</li>
 *   <li>notificationTgId: Telegram ID для уведомлений</li>
 *   <li>notifyTimes: предпочтительные временные окна для уведомлений (JSON)</li>
 *   <li>isActive: флаг активности эксперта</li>
 *   <li>directions: направления, по которым эксперт работает (many-to-many)</li>
 * </ul>
 *
 * <p>Бизнес-логика:
 * <ul>
 *   <li>Только пользователи с ролью lc_expert должны иметь запись Expert</li>
 *   <li>Деактивация эксперта (is_active = false) исключает их из назначений и останавливает уведомления</li>
 *   <li>notify_times может хранить временные диапазоны, дни недели, настройки часового пояса</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "experts")
public class Expert {
    /**
     * Идентификатор пользователя (shared primary key).
     *
     * Используется как первичный ключ и внешний ключ к users.id.
     * Значение берется из связанного объекта User через @MapsId.
     */
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Связанный пользователь (1-to-1 с shared primary key).
     *
     * Использует @MapsId для разделения первичного ключа с User.
     * Загружается лениво для оптимизации производительности.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Telegram ID для уведомлений.
     *
     * Используется для отправки уведомлений эксперту через Telegram.
     * Может быть null, если уведомления не настроены.
     */
    @Column(name = "notification_tg_id")
    private Long notificationTgId;

    /**
     * Предпочтительные временные окна для уведомлений.
     *
     * Хранится как PostgreSQL jsonb.
     * Может содержать:
     * - временные диапазоны (например, "09:00-18:00")
     * - дни недели (например, ["MONDAY", "TUESDAY", "WEDNESDAY"])
     * - настройки часового пояса
     * - другие предпочтения по времени уведомлений
     *
     * Пример структуры:
     * {
     *   "timeRanges": [{"start": "09:00", "end": "18:00"}],
     *   "weekdays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
     *   "timezone": "Europe/Moscow"
     * }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notify_times", columnDefinition = "jsonb")
    private JsonNode notifyTimes;

    /**
     * Флаг активности эксперта.
     *
     * true - эксперт активен и может получать назначения и уведомления
     * false - эксперт деактивирован (исключен из назначений, уведомления остановлены)
     *
     * По умолчанию: true
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Направления, по которым работает эксперт.
     *
     * Many-to-many связь с Direction через таблицу expert_directions.
     * Загружается лениво для оптимизации производительности.
     * Использует Set для предотвращения дубликатов.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "expert_directions",
            joinColumns = @JoinColumn(name = "expert_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "direction_id")
    )
    @Builder.Default
    private Set<Direction> directions = new HashSet<>();
}

