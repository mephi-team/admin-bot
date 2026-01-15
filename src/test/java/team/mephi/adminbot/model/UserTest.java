package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для User.
 * Покрывают: инициализацию дат и сравнение по идентификатору.
 */
class UserTest {

    /**
     * Проверяет заполнение дат и флага удаления при создании.
     */
    @Test
    void Given_newUser_When_onCreate_Then_setsDatesAndDeleted() {
        // Arrange
        User user = User.builder().build();
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setDeleted(true);

        // Act
        user.onCreate();

        // Assert
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertFalse(user.getDeleted());
    }

    /**
     * Проверяет обновление даты изменения.
     */
    @Test
    void Given_user_When_onUpdate_Then_updatesUpdatedAt() {
        // Arrange
        User user = User.builder().build();
        user.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));

        // Act
        user.onUpdate();

        // Assert
        assertNotNull(user.getUpdatedAt());
    }

    /**
     * Проверяет равенство пользователей по идентификатору.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        User first = User.builder().id(1L).build();
        User second = User.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }
}
