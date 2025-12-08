package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "dialogs")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime lastMessageAt = LocalDateTime.now();

    @Column(nullable = false)
    private Integer unreadCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}