package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для сущности StatsCache (проверка @PrePersist / @PreUpdate onUpdate).
 */
class StatsCacheTest {

    @Test
    void onUpdate_shouldSetUpdatedAtOnPersist() {
        // given
        StatsCache cache = StatsCache.builder().build();

        // when
        cache.onUpdate();

        // then
        assertNotNull(cache.getUpdatedAt(), "updatedAt должен быть установлен");
    }

    @Test
    void onUpdate_shouldRefreshUpdatedAt() throws InterruptedException {
        // given
        StatsCache cache = new StatsCache();
        cache.onUpdate();

        LocalDateTime firstUpdatedAt = cache.getUpdatedAt();

        Thread.sleep(50);

        // when
        cache.onUpdate();

        // then
        assertTrue(
                cache.getUpdatedAt().isAfter(firstUpdatedAt),
                "updatedAt должен обновляться при повторном вызове"
        );
    }
}
