package team.mephi.adminbot.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Лог аудита административных действий.
 *
 * Здесь сохраняется история того,
 * кто, что и когда сделал в системе.
 *
 * Таблица используется для:
 * - отслеживания действий администраторов и системы
 * - разборов ошибок и странного поведения
 * - отчётности и ответственности (кто нажал кнопку)
 *
 * Важно:
 * записи в этом логе никогда не изменяются.
 * Новые записи только добавляются.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table(name = "admin_audit")
public class AdminAudit {

    /**
     * Технический первичный ключ записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, который выполнил действие.
     *
     * Ссылка на users.id.
     * Связь используется только для чтения
     * и может быть пустой (например, системные действия).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor;

    /**
     * Тип сущности, над которой было выполнено действие.
     *
     * Например: USER, MAILING, DIALOG, SCRIPT_TASK.
     */
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    /**
     * ID конкретной сущности, над которой было действие.
     *
     * Может быть null, если действие не связано
     * с конкретной записью.
     */
    @Column(name = "entity_id")
    private Long entityId;

    /**
     * Название выполненного действия.
     *
     * Например: CREATE, UPDATE, ASSIGN, SEND, REASSIGN.
     */
    @Column(name = "action", nullable = false)
    private String action;

    /**
     * Дополнительные детали действия в формате JSON.
     *
     * Может содержать:
     * - состояние до и после
     * - параметры операции
     * - любые служебные данные
     *
     * Поле необязательное.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    /**
     * Дата и время, когда произошло действие.
     *
     * Заполняется автоматически при создании записи
     * и больше никогда не изменяется.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}
