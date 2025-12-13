package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import team.mephi.adminbot.model.enums.QuestionStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность вопроса пользователя.
 *
 * <p>Хранит вопросы, заданные пользователями через бота,
 * включая назначение экспертов и статус обработки.
 *
 * <p>Жизненный цикл вопроса:
 * <ol>
 *   <li><b>Создание (NEW)</b>: Вопрос создаётся пользователем с начальным статусом NEW.
 *       На этом этапе assignedExpert может быть null.</li>
 *   <li><b>Назначение эксперта (IN_PROGRESS)</b>: Когда вопрос назначается эксперту
 *       (пользователю с ролью lc_expert), статус меняется на IN_PROGRESS.</li>
 *   <li><b>Ответ (ANSWERED)</b>: Когда эксперт предоставляет ответ через UserAnswer,
 *       статус меняется на ANSWERED.</li>
 * </ol>
 *
 * <p>Важные особенности:
 * <ul>
 *   <li>Роль пользователя, задавшего вопрос, хранится в поле role (String)</li>
 *   <li>Назначение эксперта может быть изменено, история переназначений
 *       хранится в QuestionReassignLog</li>
 *   <li>Один вопрос может иметь несколько ответов (связь один-ко-многим с UserAnswer)</li>
 *   <li>Не каскадирует удаление на User или Direction</li>
 *   <li>Все связи загружаются лениво (LAZY) для оптимизации производительности</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_questions", indexes = {
        @Index(name = "idx_users_questions_status", columnList = "status"),
        @Index(name = "idx_users_questions_direction", columnList = "direction_id"),
        @Index(name = "idx_users_questions_assigned_expert", columnList = "assigned_expert_id")
})
public class UserQuestion {
    /**
     * Внутренний ID вопроса в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, который задал вопрос.
     *
     * Связь с таблицей users (users.id).
     * Обязательное поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Роль пользователя, который задал вопрос.
     *
     * Хранит роль пользователя на момент создания вопроса.
     * Обязательное поле.
     */
    @Column(nullable = false)
    private String role;

    /**
     * Направление, к которому относится вопрос.
     *
     * Связь с таблицей directions (directions.id).
     * Обязательное поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false)
    private Direction direction;

    /**
     * Текст вопроса.
     *
     * Обязательное поле, хранится как TEXT в базе данных.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Текущий статус вопроса.
     *
     * Может быть: NEW, IN_PROGRESS, ANSWERED.
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus status;

    /**
     * Эксперт, назначенный для ответа на вопрос.
     *
     * Связь с таблицей users (users.id).
     * Должен быть пользователем с ролью lc_expert.
     * Может быть null до назначения эксперта.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_expert_id")
    private User assignedExpert;

    /**
     * Дата и время создания вопроса.
     *
     * Устанавливается автоматически при создании записи.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время последнего обновления вопроса.
     *
     * Обновляется автоматически при изменении записи.
     */
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    /**
     * Ответы на этот вопрос.
     *
     * Один вопрос может иметь несколько ответов.
     * Связь один-ко-многим с UserAnswer.
     * Не каскадирует удаление (REMOVE), так как ответы должны существовать независимо от вопроса.
     */
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserAnswer> answers = new ArrayList<>();

    /**
     * Инициализация дат при создании записи.
     *
     * Используется как дополнительный механизм к @CreationTimestamp.
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
     *
     * Используется как дополнительный механизм к @UpdateTimestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // ===== equals() и hashCode() =====

    /**
     * Вопросы считаются одинаковыми, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserQuestion that = (UserQuestion) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

