package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности User (проверка @PrePersist/@PreUpdate и дефолтных коллекций).
 */
class UserTest {

    @Test
    void newUser_shouldHaveEmptyDialogsAndMessagesLists() {
        // given / when
        User user = new User();

        // then
        assertNotNull(user.getDialogs(), "dialogs не должен быть null по умолчанию");
        assertNotNull(user.getMessages(), "messages не должен быть null по умолчанию");
        assertTrue(user.getDialogs().isEmpty(), "dialogs должен быть пустым по умолчанию");
        assertTrue(user.getMessages().isEmpty(), "messages должен быть пустым по умолчанию");
    }

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAtToNow() {
        // given
        User user = new User();

        assertNull(user.getCreatedAt(), "До onCreate createdAt должен быть null");
        assertNull(user.getUpdatedAt(), "До onCreate updatedAt должен быть null");

        // when
        user.onCreate();

        // then
        assertNotNull(user.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertNotNull(user.getUpdatedAt(), "После onCreate updatedAt должен быть установлен");

        assertEquals(user.getCreatedAt(), user.getUpdatedAt(), "createdAt и updatedAt при создании должны совпадать");

        assertTrue(
                Duration.between(user.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
    }

    @Test
    void onUpdate_shouldUpdateOnlyUpdatedAt() throws InterruptedException {
        // given
        User user = new User();
        user.onCreate();

        LocalDateTime createdAtBefore = user.getCreatedAt();
        LocalDateTime updatedAtBefore = user.getUpdatedAt();

        // небольшая пауза, чтобы updatedAt точно отличался
        Thread.sleep(10);

        // when
        user.onUpdate();

        // then
        assertEquals(createdAtBefore, user.getCreatedAt(), "createdAt не должен меняться при onUpdate");
        assertTrue(
                user.getUpdatedAt().isAfter(updatedAtBefore),
                "updatedAt должен обновиться и стать позже предыдущего значения"
        );
    }
}
