package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Сущность назначения студента на тьютора.
 *
 * Представляет историю назначений студентов на тьюторов.
 * Одна запись = одно назначение студента на тьютора.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_tutor")
@IdClass(StudentTutor.StudentTutorId.class)
public class StudentTutor {

    @Id
    @Column(name = "tutor_id", nullable = false)
    private Long tutorId;

    /**
     * Тьютор, которому назначен студент.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false, insertable = false, updatable = false)
    private Tutor tutor;

    @Id
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * Студент (пользователь), назначенный на тьютора.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, insertable = false, updatable = false)
    private User student;

    /**
     * Составной ключ для связи студента и тьютора.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentTutorId implements Serializable {
        private Long tutorId;
        private Long studentId;
    }
}

