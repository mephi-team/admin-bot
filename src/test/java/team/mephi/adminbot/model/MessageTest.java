package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MessageStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Message (проверка дефолтных временных полей и @PreUpdate).
 */
class MessageTest {

    @Test
    void newMessage_shouldHaveDefaultTimestamps() {
        // given / when
        Message message = new Message();

        // then
        assertNotNull(message.getCreatedAt(), "createdAt должен быть инициализирован по умолчанию");
        assertNotNull(message.getUpdatedAt(), "updatedAt должен быть инициализирован по умолчанию");

        assertTrue(
                Duration.between(message.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
        assertTrue(
                Duration.between(message.getUpdatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "updatedAt должен быть примерно текущим временем"
        );
    }

    @Test
    void setUpdatedAt_shouldOverrideDefaultValue() {
        // given
        Message message = new Message();
        LocalDateTime newUpdatedAt = LocalDateTime.now().minusMinutes(10);

        // when
        message.setUpdatedAt(newUpdatedAt);

        // then
        assertEquals(newUpdatedAt, message.getUpdatedAt(), "updatedAt должен обновляться сеттером");
    }

    @Test
    void onUpdate_shouldRefreshUpdatedAt() throws InterruptedException {
        // given
        Message message = new Message();
        LocalDateTime before = message.getUpdatedAt();

        Thread.sleep(50);

        // when
        message.onUpdate();

        // then
        assertTrue(message.getUpdatedAt().isAfter(before), "updatedAt должен обновиться при onUpdate");
    }

    // ===== Тесты статусов сообщений =====

    @Test
    void message_shouldAcceptSENTStatus() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.SENT);

        // then
        assertEquals(MessageStatus.SENT, message.getStatus(), "Сообщение должно принимать статус SENT");
    }

    @Test
    void message_shouldAcceptDELIVEREDStatus() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.DELIVERED);

        // then
        assertEquals(MessageStatus.DELIVERED, message.getStatus(), "Сообщение должно принимать статус DELIVERED");
    }

    @Test
    void message_shouldAcceptREADStatus() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.READ);

        // then
        assertEquals(MessageStatus.READ, message.getStatus(), "Сообщение должно принимать статус READ");
    }

    @Test
    void message_shouldAcceptFAILEDStatus() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.FAILED);

        // then
        assertEquals(MessageStatus.FAILED, message.getStatus(), "Сообщение должно принимать статус FAILED");
    }

    @Test
    void messageWithFAILEDStatus_shouldHaveStatusReason() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("Telegram API error: user blocked the bot");

        // then
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertEquals("Telegram API error: user blocked the bot", message.getStatusReason(), 
            "При статусе FAILED должна заполняться причина ошибки");
    }

    @Test
    void messageWithSENTStatus_canHaveNullStatusReason() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.SENT);
        message.setStatusReason(null);

        // then
        assertEquals(MessageStatus.SENT, message.getStatus());
        assertNull(message.getStatusReason(), 
            "Для статуса SENT причина ошибки может быть null");
    }

    @Test
    void messageWithDELIVEREDStatus_canHaveNullStatusReason() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.DELIVERED);
        message.setStatusReason(null);

        // then
        assertEquals(MessageStatus.DELIVERED, message.getStatus());
        assertNull(message.getStatusReason(), 
            "Для статуса DELIVERED причина ошибки может быть null");
    }

    @Test
    void messageWithREADStatus_canHaveNullStatusReason() {
        // given / when
        Message message = new Message();
        message.setStatus(MessageStatus.READ);
        message.setStatusReason(null);

        // then
        assertEquals(MessageStatus.READ, message.getStatus());
        assertNull(message.getStatusReason(), 
            "Для статуса READ причина ошибки может быть null");
    }

    @Test
    void message_shouldAllowStatusTransitionFromSENTToFAILED() {
        // given
        Message message = new Message();
        message.setStatus(MessageStatus.SENT);

        // when
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("Connection timeout");

        // then
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertEquals("Connection timeout", message.getStatusReason());
    }

    @Test
    void message_shouldAllowStatusTransitionFromSENTToREAD() {
        // given
        Message message = new Message();
        message.setStatus(MessageStatus.SENT);

        // when - переход через DELIVERED не требуется, проверяем прямой переход
        message.setStatus(MessageStatus.READ);

        // then
        assertEquals(MessageStatus.READ, message.getStatus());
    }
}
