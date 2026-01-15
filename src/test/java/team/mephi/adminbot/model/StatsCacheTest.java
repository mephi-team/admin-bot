package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Юнит-тесты для StatsCache.
 * Покрывают: обновление времени при сохранении.
 */
class StatsCacheTest {

    /**
     * Проверяет установку времени обновления при хуке.
     */
    @Test
    void Given_entity_When_onUpdate_Then_setsUpdatedAt() {
        // Arrange
        StatsCache cache = StatsCache.builder().metricName("m").period("p").payload("{}").build();
        cache.setUpdatedAt(null);

        // Act
        cache.onUpdate();

        // Assert
        assertNotNull(cache.getUpdatedAt());
    }
}
