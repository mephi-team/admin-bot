package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для Tutor.
 * Покрывают: инициализацию флага удаления и equals.
 */
class TutorTest {

    /**
     * Проверяет установку флага блокировки при создании.
     */
    @Test
    void Given_newTutor_When_onCreate_Then_setsBlockedFalse() {
        // Arrange
        Tutor tutor = Tutor.builder().build();
        tutor.setBlocked(true);

        // Act
        tutor.onCreate();

        // Assert
        assertFalse(tutor.getBlocked());
    }

    /**
     * Проверяет равенство тьюторов по идентификатору.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Tutor first = Tutor.builder().id(1L).build();
        Tutor second = Tutor.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }
}
