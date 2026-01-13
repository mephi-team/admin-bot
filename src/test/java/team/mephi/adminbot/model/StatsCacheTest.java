package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для сущности {@link StatsCache}.
 */
class StatsCacheTest {

    /**
     * Проверяет, что onUpdate устанавливает метку времени обновления.
     */
    @Test
    void givenCache_WhenOnUpdateCalled_ThenUpdatedAtIsSet() {
        // Arrange
        StatsCache cache = StatsCache.builder().build();

        // Act
        cache.onUpdate();

        // Assert
        assertNotNull(cache.getUpdatedAt(), "updatedAt должен быть установлен");
    }
}
