package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности MailingTask (проверка Lombok-методов и полей).
 */
class MailingTaskTest {

    @Test
    void builder_shouldSetFields() {
        // given
        Mailing mailing = new Mailing();
        mailing.setId(1L);

        LocalDateTime sendAt = LocalDateTime.now().plusDays(1);

        // when
        MailingTask task = MailingTask.builder()
                .id(2L)
                .mailing(mailing)
                .sendAt(sendAt)
                .repeatCron("0 0 * * *")
                .status(MailingStatus.SCHEDULED)
                .build();

        // then
        assertEquals(2L, task.getId());
        assertSame(mailing, task.getMailing());
        assertEquals(sendAt, task.getSendAt());
        assertEquals("0 0 * * *", task.getRepeatCron());
        assertEquals(MailingStatus.SCHEDULED, task.getStatus());
        assertNotNull(task.toString());
    }
}
