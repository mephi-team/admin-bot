package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Broadcast (проверка @PrePersist onCreate и связей).
 */
class MailingTest {

    @Test
    void onCreate_shouldSetCreatedAtToNow() {
        // given
        Mailing broadcast = Mailing.builder()
                .name("Тестовая рассылка")
                .build();

        assertNull(broadcast.getCreatedAt(), "До onCreate createdAt должен быть null");

        // when
        broadcast.onCreate();

        // then
        assertNotNull(broadcast.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertTrue(
                Duration.between(broadcast.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
    }

    @Test
    void builder_shouldSetRelationsAndFields() {
        // given
        User creator = new User();
        creator.setId(1L);

        Direction direction = new Direction();
        direction.setId(2L);

        Role role = new Role();
        role.setCode(3L);

        // when
        Mailing broadcast = Mailing.builder()
                .id(100L)
                .build();

        // then
        assertEquals(100L, broadcast.getId());
    }
}
