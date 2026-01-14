package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для Direction.
 * Покрывают: сравнение направлений по идентификатору.
 */
class DirectionTest {

    /**
     * Проверяет равенство направлений при одинаковом идентификаторе.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Direction first = Direction.builder().id(1L).build();
        Direction second = Direction.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство направлений при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        Direction first = Direction.builder().id(1L).build();
        Direction second = Direction.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
