package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Message (проверка дефолтных временных полей).
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
}
