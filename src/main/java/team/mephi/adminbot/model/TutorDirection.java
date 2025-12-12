package team.mephi.adminbot.model;


import jakarta.persistence.*;

@Entity
@Table(name = "tutor_directions")
public class TutorDirection {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false)
    private Direction direction;
}
