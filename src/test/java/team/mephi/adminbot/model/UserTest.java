package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для сущности {@link User}.
 */
class UserTest {

    /**
     * Проверяет, что у нового пользователя пустые коллекции сообщений и диалогов.
     */
    @Test
    void givenNewUser_WhenCreated_ThenCollectionsEmpty() {
        // Arrange
        User user = new User();

        // Act
        var dialogs = user.getDialogs();
        var messages = user.getMessages();

        // Assert
        assertNotNull(dialogs);
        assertNotNull(messages);
        assertTrue(dialogs.isEmpty());
        assertTrue(messages.isEmpty());
    }
}
