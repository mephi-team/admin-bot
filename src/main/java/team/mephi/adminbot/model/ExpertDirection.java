package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expert_directions")
@IdClass(ExpertDirection.ExpertDirectionId.class)
public class ExpertDirection {
    @Id
    @Column(name = "expert_id", nullable = false)
    private Long expertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false, insertable = false, updatable = false)
    private Expert expert;

    @Id
    @Column(name = "direction_id", nullable = false)
    private Long directionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false, insertable = false, updatable = false)
    private Direction direction;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertDirectionId implements Serializable {
        private Long expertId;
        private Long directionId;
    }
}

