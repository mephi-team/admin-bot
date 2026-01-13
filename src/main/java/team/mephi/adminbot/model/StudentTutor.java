package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.StudentTutorMode;

import java.time.Instant;

/**
 * Сущность назначения студента на тьютора.
 * <p>
 * Представляет историю назначений студентов на тьюторов.
 * Одна запись = одно назначение студента на тьютора.
 * <p>
 * Правила:
 * - student_id и tutor_id являются обязательными
 * - Для каждого студента должна существовать только одна активная запись (is_active = true)
 * (правило обеспечивается на уровне сервиса или базы данных)
 * - Исторические записи являются неизменяемыми после создания
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_tutor")
public class StudentTutor {

    /**
     * Уникальный идентификатор записи назначения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Студент (пользователь), назначенный на тьютора.
     * <p>
     * Связь многие-к-одному: один студент может иметь множество записей о назначениях.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * Тьютор, которому назначен студент.
     * <p>
     * Связь многие-к-одному: один тьютор может иметь множество записей о назначениях студентов.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    /**
     * Дата и время назначения студента на тьютора.
     */
    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    /**
     * Режим назначения: первичное (INITIAL) или повторное (REASSIGN).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private StudentTutorMode mode;

    /**
     * Флаг активности назначения.
     * <p>
     * true - назначение активно (текущее)
     * false - назначение неактивно (историческое)
     * <p>
     * Правило: для каждого студента должна существовать только одна активная запись.
     * Это правило обеспечивается на уровне сервиса или базы данных.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Инициализация даты назначения при создании записи.
     */
    @PrePersist
    protected void onCreate() {
        if (this.assignedAt == null) {
            this.assignedAt = Instant.now();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    /**
     * Записи назначений считаются равными,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentTutor that = (StudentTutor) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
