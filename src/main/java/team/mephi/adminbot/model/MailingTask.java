package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mailing_task")
public class MailingTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailing_id", nullable = false)
    private Mailing mailing;

    @Column(name = "send_at")
    private LocalDateTime sendAt;

    @Column(name = "repeat_cron")
    private String repeatCron;

    @Column(name = "repeat_until")
    private LocalDateTime repeatUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingStatus status;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;
}

