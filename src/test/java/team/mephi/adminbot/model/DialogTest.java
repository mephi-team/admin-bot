package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Dialog (проверка дефолтных значений и @PrePersist/@PreUpdate).
 */
class DialogTest {

    @Test
    void newDialog_shouldHaveDefaultLastMessageAtAndUnreadCount() {
        // given / when
        Dialog dialog = new Dialog();

        // then
        assertNotNull(dialog.getLastMessageAt(), "lastMessageAt должен иметь дефолтное значение");
        assertNotNull(dialog.getUnreadCount(), "unreadCount должен иметь дефолтное значение");
        assertEquals(0, dialog.getUnreadCount(), "unreadCount по умолчанию должен быть 0");
    }

    @Test
    void setLastMessageAt_shouldOverrideDefaultValue() {
        // given
        Dialog dialog = new Dialog();
        LocalDateTime newTime = LocalDateTime.now().minusHours(2);

        // when
        dialog.setLastMessageAt(newTime);

        // then
        assertEquals(newTime, dialog.getLastMessageAt(), "lastMessageAt должен обновляться сеттером");
    }

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAtToNow() {
        // given
        Dialog dialog = new Dialog();

        assertNull(dialog.getCreatedAt(), "До onCreate createdAt должен быть null");
        assertNull(dialog.getUpdatedAt(), "До onCreate updatedAt должен быть null");

        // when
        dialog.onCreate();

        // then
        assertNotNull(dialog.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertNotNull(dialog.getUpdatedAt(), "После onCreate updatedAt должен быть установлен");
        assertEquals(dialog.getCreatedAt(), dialog.getUpdatedAt(), "createdAt и updatedAt при создании должны совпадать");

        assertTrue(
                Duration.between(dialog.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
    }

    @Test
    void onUpdate_shouldUpdateOnlyUpdatedAt() throws InterruptedException {
        // given
        Dialog dialog = new Dialog();
        dialog.onCreate();

        LocalDateTime createdAtBefore = dialog.getCreatedAt();
        LocalDateTime updatedAtBefore = dialog.getUpdatedAt();

        Thread.sleep(50);

        // when
        dialog.onUpdate();

        // then
        assertEquals(createdAtBefore, dialog.getCreatedAt(), "createdAt не должен меняться при onUpdate");
        assertTrue(dialog.getUpdatedAt().isAfter(updatedAtBefore), "updatedAt должен обновиться");
    }
}
