package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности User.
 */
class UserTest {

    @Test
    void newUser_shouldHaveEmptyCollections() {
        User user = new User();

        assertNotNull(user.getDialogs());
        assertNotNull(user.getMessages());
        assertTrue(user.getDialogs().isEmpty());
        assertTrue(user.getMessages().isEmpty());
    }

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAt() {
        User user = new User();

        user.onCreate();

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());

        assertTrue(
                Duration.between(user.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5
        );
    }

    @Test
    void onUpdate_shouldChangeOnlyUpdatedAt() throws InterruptedException {
        User user = new User();
        user.onCreate();

        LocalDateTime createdAt = user.getCreatedAt();
        LocalDateTime updatedAtBefore = user.getUpdatedAt();

        Thread.sleep(50);

        user.onUpdate();

        assertEquals(createdAt, user.getCreatedAt());
        assertTrue(user.getUpdatedAt().isAfter(updatedAtBefore));
    }
}
