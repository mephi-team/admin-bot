package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для Application.
 * Покрывают: инициализацию дат и сравнение по идентификатору.
 */
class ApplicationTest {

    /**
     * Проверяет заполнение дат при создании.
     */
    @Test
    void Given_newApplication_When_onCreate_Then_setsDates() {
        // Arrange
        Application application = Application.builder().build();
        application.setCreatedAt(null);
        application.setUpdatedAt(null);

        // Act
        application.onCreate();

        // Assert
        assertNotNull(application.getCreatedAt());
        assertNotNull(application.getUpdatedAt());
    }

    /**
     * Проверяет обновление даты изменения.
     */
    @Test
    void Given_application_When_onUpdate_Then_updatesUpdatedAt() {
        // Arrange
        Application application = Application.builder().build();
        application.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));

        // Act
        application.onUpdate();

        // Assert
        assertNotNull(application.getUpdatedAt());
    }

    /**
     * Проверяет равенство заявок по идентификатору.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Application first = Application.builder().id(1L).build();
        Application second = Application.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство заявок при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        Application first = Application.builder().id(1L).build();
        Application second = Application.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
