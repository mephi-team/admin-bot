package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.AnswerStatus;

import java.time.Instant;

/**
 * Сущность ответа на вопрос пользователя.
 *
 * <p>Хранит ответы, предоставленные администраторами или экспертами
 * на вопросы, заданные пользователями через бота.
 *
 * <p>Жизненный цикл ответа:
 * <ol>
 *   <li><b>Создание (DRAFT)</b>: Ответ создаётся администратором или экспертом
 *       (пользователем с ролью admin или lc_expert) с начальным статусом DRAFT.</li>
 *   <li><b>Отправка (SENT)</b>: Когда ответ отправляется пользователю,
 *       статус меняется на SENT.</li>
 *   <li><b>Обновление (UPDATED)</b>: Если ответ был изменён после отправки,
 *       статус меняется на UPDATED.</li>
 * </ol>
 *
 * <p>Важные особенности:
 * <ul>
 *   <li>Ответ может быть создан только для существующего вопроса (UserQuestion)</li>
 *   <li>Только пользователи с ролью admin или lc_expert могут быть answeredBy</li>
 *   <li>Один вопрос может иметь несколько ответов (история изменений)</li>
 *   <li>Не каскадирует удаление на UserQuestion или User</li>
 *   <li>Все связи загружаются лениво (LAZY) для оптимизации производительности</li>
 *   <li>Поле answeredAt устанавливается явно в бизнес-логике, не автоматически</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_answer")
public class UserAnswer {
    /**
     * Внутренний ID ответа в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Вопрос, на который дан ответ.
     *
     * Связь с таблицей users_questions (users_questions.id).
     * Обязательное поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private UserQuestion question;

    /**
     * Текст ответа.
     *
     * Обязательное поле, хранится как TEXT в базе данных.
     */
    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;

    /**
     * Пользователь, который предоставил ответ.
     *
     * Связь с таблицей users (users.id).
     * Должен быть пользователем с ролью admin или lc_expert.
     * Обязательное поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answered_by", nullable = false)
    private User answeredBy;

    /**
     * Дата и время, когда был предоставлен ответ.
     *
     * Обязательное поле. Устанавливается явно в бизнес-логике.
     */
    @Column(name = "answered_at", nullable = false)
    private Instant answeredAt;

    /**
     * Текущий статус ответа.
     *
     * Может быть: DRAFT, SENT, UPDATED.
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerStatus status;

    // ===== equals() и hashCode() =====

    /**
     * Ответы считаются одинаковыми, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAnswer that = (UserAnswer) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

