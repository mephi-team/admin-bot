package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.Channels;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.model.objects.Filters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mailings")
public class Mailing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Channels> channels;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Filters filters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private MailTemplate template;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reason_code", columnDefinition = "jsonb")
    private Object reasonCode;

    @OneToMany(mappedBy = "mailing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MailingTask> tasks;

    @OneToMany(mappedBy = "mailing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MailingRecipient> recipients;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

