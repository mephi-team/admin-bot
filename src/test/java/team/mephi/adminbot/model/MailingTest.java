package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты для сущности Mailing.
 */
class MailingTest {

//    @Test
//    void onCreate_shouldSetCreatedAtToNow() {
//        // given
//        Mailing mailing = Mailing.builder()
//                .name("Тестовая рассылка")
//                .build();
//
//        assertNull(mailing.getCreatedAt());
//
//        // when
//        mailing.onCreate();
//
//        // then
//        assertNotNull(mailing.getCreatedAt());
//        assertTrue(
//                Duration.between(mailing.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5
//        );
//    }

    @Test
    void builder_shouldSetIdAndName() {
        // given / when
        Mailing mailing = Mailing.builder()
                .id(100L)
                .name("Mailing name")
                .build();

        // then
        assertEquals(100L, mailing.getId());
        assertEquals("Mailing name", mailing.getName());
    }
}
