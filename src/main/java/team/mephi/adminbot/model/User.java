package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tgId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String dateOfBirth;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String status; // например: "active", "blocked"

    @Column
    private Boolean userApproval = false;

    @Column
    private LocalDateTime pdConsentDate;

    @Column
    private Boolean isRevoked = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Dialog> dialogs = new ArrayList<>();

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction", nullable = true)
    private Direction direction;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
