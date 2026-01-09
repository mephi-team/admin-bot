package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.RegistrationAction;
import team.mephi.adminbot.model.enums.RegistrationStatus;

import java.time.Instant;

/**
 * Лог регистрации пользователя.
 *
 * <p>Эта сущность хранит историю процессов регистрации и выполнения скриптов регистрации.
 * Записи в этой таблице являются append-only (только добавление, без обновлений после вставки).
 *
 * <p>Каждая запись содержит:
 * <ul>
 *   <li>Действие, которое было выполнено (регистрация, запуск скрипта и т.д.)</li>
 *   <li>Статус выполнения операции</li>
 *   <li>Дополнительные данные в формате JSON</li>
 *   <li>Временную метку создания записи</li>
 * </ul>
 *
 * <p>Связь с пользователем: один пользователь может иметь множество записей в логе регистрации.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "registration_log", indexes = {
        @Index(name = "idx_registration_log_user_id", columnList = "user_id"),
        @Index(name = "idx_registration_log_action", columnList = "action"),
        @Index(name = "idx_registration_log_created_at", columnList = "created_at")
})
public class RegistrationLog {

    /**
     * Уникальный идентификатор записи лога.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Пользователь, для которого выполняется операция регистрации.
     * <p>
     * Связь: Many-to-One (много записей лога для одного пользователя).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к User).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Действие, которое было выполнено в процессе регистрации.
     * <p>
     * Примеры: REGISTER, IDP_PENDING, SCRIPT_STARTED, SCRIPT_FINISHED и т.д.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationAction action;

    /**
     * Дополнительные данные операции в формате JSON.
     * <p>
     * Хранится в PostgreSQL как jsonb для эффективной работы с JSON-данными.
     * Может содержать произвольную структуру данных, специфичную для конкретного действия.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object payload;

    /**
     * Статус выполнения операции регистрации.
     * <p>
     * Может быть: SUCCESS, FAILED, IN_PROGRESS.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;

    /**
     * Дата и время создания записи лога.
     * <p>
     * Устанавливается автоматически при создании записи и не может быть изменена после вставки.
     * Это поле является неизменяемым (immutable) для обеспечения целостности аудита.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}

