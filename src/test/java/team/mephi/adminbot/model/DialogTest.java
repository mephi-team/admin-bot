package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Dialog (проверка дефолтных значений полей).
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
}
