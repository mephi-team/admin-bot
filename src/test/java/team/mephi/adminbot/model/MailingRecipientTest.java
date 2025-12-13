package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingChannel;
import team.mephi.adminbot.model.enums.MailingRecipientStatus;

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
                .channel(MailingChannel.TELEGRAM)
                .status(MailingRecipientStatus.SENT)
                .statusReason("ok")
                .messageId("mid")
                .build();

        // then
        assertEquals(3L, mr.getId());
        assertSame(user, mr.getUser());
        assertSame(mailing, mr.getMailing());
        assertEquals(MailingChannel.TELEGRAM, mr.getChannel());
        assertEquals(MailingRecipientStatus.SENT, mr.getStatus());
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
