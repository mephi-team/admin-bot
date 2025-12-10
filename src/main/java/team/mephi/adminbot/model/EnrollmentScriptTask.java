package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollment_script_tasks")
public class EnrollmentScriptTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private EnrollmentScriptFile file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScriptTaskStatus status;

    @Column(name = "success_count")
    private Integer successCount = 0;

    @Column(name = "error_count")
    private Integer errorCount = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object log;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}

