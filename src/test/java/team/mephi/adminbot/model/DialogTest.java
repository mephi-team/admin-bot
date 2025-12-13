package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.DialogStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Dialog (проверка дефолтных значений и полей).
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
        assertEquals(DialogStatus.ACTIVE, dialog.getStatus(), "status по умолчанию должен быть ACTIVE");
    }

    @Test
    void setLastMessageAt_shouldOverrideDefaultValue() {
        // given
        Dialog dialog = new Dialog();
        Instant newTime = Instant.now().minusSeconds(2 * 3600);

        // when
        dialog.setLastMessageAt(newTime);

        // then
        assertEquals(newTime, dialog.getLastMessageAt(), "lastMessageAt должен обновляться сеттером");
    }

    @Test
    void setStatus_shouldUpdateStatus() {
        // given
        Dialog dialog = new Dialog();

        // when
        dialog.setStatus(DialogStatus.CLOSED);

        // then
        assertEquals(DialogStatus.CLOSED, dialog.getStatus(), "status должен обновляться сеттером");
    }

    @Test
    void setUnreadCount_shouldUpdateUnreadCount() {
        // given
        Dialog dialog = new Dialog();

        // when
        dialog.setUnreadCount(5);

        // then
        assertEquals(5, dialog.getUnreadCount(), "unreadCount должен обновляться сеттером");
    }

    @Test
    void equals_shouldCompareById() {
        // given
        Dialog dialog1 = new Dialog();
        dialog1.setId(1L);

        Dialog dialog2 = new Dialog();
        dialog2.setId(1L);

        Dialog dialog3 = new Dialog();
        dialog3.setId(2L);

        // then
        assertEquals(dialog1, dialog2, "Диалоги с одинаковым ID должны быть равны");
        assertNotEquals(dialog1, dialog3, "Диалоги с разным ID должны быть не равны");
    }

    @Test
    void hashCode_shouldBeConsistent() {
        // given
        Dialog dialog = new Dialog();
        dialog.setId(1L);

        // when / then
        int hashCode1 = dialog.hashCode();
        int hashCode2 = dialog.hashCode();

        assertEquals(hashCode1, hashCode2, "hashCode должен быть консистентным");
    }
}
