package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

/**
 * Кэшированные статистические данные.
 *
 * <p>Эта сущность используется для хранения предварительно вычисленных
 * статистических данных, которые могут быть быстро извлечены без необходимости
 * повторного вычисления.
 *
 * <p>Каждая запись в таблице идентифицируется комбинацией имени метрики (metricName)
 * и периода (period), что позволяет хранить различные статистики для разных
 * временных интервалов.
 *
 * <p>Поле payload содержит сами статистические данные в формате JSON,
 * что обеспечивает гибкость в хранении различных структур данных.
 *
 * <p>Поле updatedAt автоматически обновляется при создании или обновлении записи,
 * что позволяет отслеживать актуальность кэшированных данных.
 */
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

