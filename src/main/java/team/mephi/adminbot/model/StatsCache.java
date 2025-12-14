package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats_cache")
@IdClass(StatsCache.StatsCacheId.class)
public class StatsCache {
    @Id
    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Id
    @Column(name = "period", nullable = false)
    private String period;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Object payload;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatsCacheId implements java.io.Serializable {
        private String metricName;
        private String period;
    }
}

