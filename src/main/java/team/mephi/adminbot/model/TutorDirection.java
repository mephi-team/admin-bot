package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Сущность связи между тьюторами и направлениями.
 * <p>
 * Представляет многие-ко-многим связь между Tutor и Direction
 * через таблицу tutor_directions.
 * <p>
 * Использует композитный первичный ключ (tutor_id, direction_id).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutor_directions")
@IdClass(TutorDirection.TutorDirectionId.class)
public class TutorDirection {
    @Id
    @Column(name = "tutor_id", nullable = false)
    private Long tutorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false, insertable = false, updatable = false)
    private Tutor tutor;

    @Id
    @Column(name = "direction_id", nullable = false)
    private Long directionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false, insertable = false, updatable = false)
    private Direction direction;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TutorDirectionId implements Serializable {
        private Long tutorId;
        private Long directionId;
    }
}

