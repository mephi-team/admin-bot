package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tutors")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String userName;

    @Column
    private String tgId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column
    private String notes;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<StudentTutor> studentTutor;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<TutorDirection> tutorDirections;
}
