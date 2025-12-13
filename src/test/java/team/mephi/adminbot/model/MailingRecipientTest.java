package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.RecipientStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности MailingRecipient (проверка Lombok-методов и полей).
 */
class MailingRecipientTest {

    @Test
    void builder_shouldSetFields() {
        // given
        User user = new User();
        user.setId(1L);

        Mailing mailing = new Mailing();
        mailing.setId(2L);

        // when
        MailingRecipient mr = MailingRecipient.builder()
                .id(3L)
                .user(user)
                .mailing(mailing)
                .channel("TELEGRAM")
                .status(RecipientStatus.SENT)
                .statusReason("ok")
                .messageId("mid")
                .build();

        // then
        assertEquals(3L, mr.getId());
        assertSame(user, mr.getUser());
        assertSame(mailing, mr.getMailing());
        assertEquals("TELEGRAM", mr.getChannel());
        assertEquals(RecipientStatus.SENT, mr.getStatus());
        assertEquals("ok", mr.getStatusReason());
        assertEquals("mid", mr.getMessageId());
        assertNotNull(mr.toString());
    }

    @Test
    void equalsHashCode_shouldWork() {
        // given
        MailingRecipient a = MailingRecipient.builder().id(1L).build();
        MailingRecipient b = MailingRecipient.builder().id(1L).build();

        // when / then
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
