package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Лог переназначения вопросов между направлениями обучения.
 *
 * <p>Эта сущность хранит историю переназначений вопросов между направлениями обучения,
 * включая причину переназначения и администратора/эксперта, который выполнил переназначение.
 * Записи в этой таблице являются append-only (только добавление, без обновлений после вставки).
 *
 * <p>Каждая запись представляет одно событие переназначения и содержит:
 * <ul>
 *   <li>Вопрос, который был переназначен</li>
 *   <li>Направление, с которого был переназначен вопрос (from_direction_id)</li>
 *   <li>Направление, на которое был переназначен вопрос (to_direction_id)</li>
 *   <li>Причину переназначения (опционально)</li>
 *   <li>Пользователя (администратора/эксперта), который выполнил переназначение</li>
 *   <li>Временную метку создания записи</li>
 * </ul>
 *
 * <p>Важные особенности:
 * <ul>
 *   <li>Данные неизменяемы после вставки (immutable)</li>
 *   <li>from_direction_id не должно равняться to_direction_id (бизнес-правило)</li>
 *   <li>created_at устанавливается автоматически при создании записи</li>
 *   <li>Не каскадирует удаление на связанные сущности (UserQuestion, Direction, User)</li>
 *   <li>Все связи загружаются лениво (LAZY) для оптимизации производительности</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "question_reassign_log")
public class QuestionReassignLog {
    /**
     * Уникальный идентификатор записи лога переназначения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Вопрос, который был переназначен.
     * <p>
     * Связь: Many-to-One (много записей лога для одного вопроса).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к UserQuestion).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private UserQuestion question;

    /**
     * Направление обучения, с которого был переназначен вопрос.
     * <p>
     * Связь: Many-to-One (много записей лога для одного направления).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к Direction).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_direction_id", nullable = false)
    private Direction fromDirection;

    /**
     * Направление обучения, на которое был переназначен вопрос.
     * <p>
     * Связь: Many-to-One (много записей лога для одного направления).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к Direction).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_direction_id", nullable = false)
    private Direction toDirection;

    /**
     * Причина переназначения вопроса.
     * <p>
     * Опциональное текстовое поле, содержащее объяснение причины переназначения.
     * Хранится в базе данных как TEXT для поддержки длинных текстов.
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    /**
     * Пользователь (администратор или эксперт), который выполнил переназначение.
     * <p>
     * Связь: Many-to-One (много записей лога для одного пользователя).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к User).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reassigned_by", nullable = false)
    private User reassignedBy;

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

