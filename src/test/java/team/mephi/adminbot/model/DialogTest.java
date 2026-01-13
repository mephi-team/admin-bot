package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.DialogStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для сущности {@link Dialog}.
 */
class DialogTest {

    /**
     * Проверяет значения по умолчанию у нового диалога.
     */
    @Test
    void givenNewDialog_WhenCreated_ThenDefaultsApplied() {
        // Arrange
        Dialog dialog = new Dialog();

        // Act
        Instant lastMessageAt = dialog.getLastMessageAt();
        Integer unreadCount = dialog.getUnreadCount();
        DialogStatus status = dialog.getStatus();

        // Assert
        assertNotNull(lastMessageAt, "lastMessageAt должен иметь дефолтное значение");
        assertNotNull(unreadCount, "unreadCount должен иметь дефолтное значение");
        assertEquals(0, unreadCount, "unreadCount по умолчанию должен быть 0");
        assertEquals(DialogStatus.ACTIVE, status, "status по умолчанию должен быть ACTIVE");
    }

    /**
     * Проверяет обновление времени последнего сообщения.
     */
    @Test
    void givenDialog_WhenLastMessageAtUpdated_ThenValueChanges() {
        // Arrange
        Dialog dialog = new Dialog();
        Instant newTime = Instant.now().minusSeconds(2 * 3600);

        // Act
        dialog.setLastMessageAt(newTime);

        // Assert
        assertEquals(newTime, dialog.getLastMessageAt(), "lastMessageAt должен обновляться сеттером");
    }

    /**
     * Проверяет обновление статуса диалога.
     */
    @Test
    void givenDialog_WhenStatusUpdated_ThenValueChanges() {
        // Arrange
        Dialog dialog = new Dialog();

        // Act
        dialog.setStatus(DialogStatus.CLOSED);

        // Assert
        assertEquals(DialogStatus.CLOSED, dialog.getStatus(), "status должен обновляться сеттером");
    }

    /**
     * Проверяет обновление количества непрочитанных сообщений.
     */
    @Test
    void givenDialog_WhenUnreadCountUpdated_ThenValueChanges() {
        // Arrange
        Dialog dialog = new Dialog();

        // Act
        dialog.setUnreadCount(5);

        // Assert
        assertEquals(5, dialog.getUnreadCount(), "unreadCount должен обновляться сеттером");
    }

    /**
     * Проверяет сравнение диалогов по идентификатору.
     */
    @Test
    void givenDialogsWithIds_WhenCompared_ThenEqualityUsesId() {
        // Arrange
        Dialog dialog1 = new Dialog();
        dialog1.setId(1L);

        Dialog dialog2 = new Dialog();
        dialog2.setId(1L);

        Dialog dialog3 = new Dialog();
        dialog3.setId(2L);

        // Act
        boolean sameIdEquals = dialog1.equals(dialog2);
        boolean differentIdEquals = dialog1.equals(dialog3);

        // Assert
        assertEquals(true, sameIdEquals, "Диалоги с одинаковым ID должны быть равны");
        assertNotEquals(true, differentIdEquals, "Диалоги с разным ID должны быть не равны");
    }

    /**
     * Проверяет консистентность hashCode для одной и той же сущности.
     */
    @Test
    void givenDialog_WhenHashCodeCalled_ThenConsistent() {
        // Arrange
        Dialog dialog = new Dialog();
        dialog.setId(1L);

        // Act
        int hashCode1 = dialog.hashCode();
        int hashCode2 = dialog.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2, "hashCode должен быть консистентным");
    }
}
