package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingChannel;
import team.mephi.adminbot.model.enums.MailingRecipientStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Тесты для сущности {@link MailingRecipient}.
 */
class MailingRecipientTest {

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Mailing mailing = new Mailing();
        mailing.setId(2L);

        // Act
        MailingRecipient recipient = MailingRecipient.builder()
                .id(3L)
                .user(user)
                .mailing(mailing)
                .channel(MailingChannel.TELEGRAM)
                .status(MailingRecipientStatus.SENT)
                .statusReason("ok")
                .messageId("mid")
                .build();

        // Assert
        assertEquals(3L, recipient.getId());
        assertSame(user, recipient.getUser());
        assertSame(mailing, recipient.getMailing());
        assertEquals(MailingChannel.TELEGRAM, recipient.getChannel());
        assertEquals(MailingRecipientStatus.SENT, recipient.getStatus());
        assertEquals("ok", recipient.getStatusReason());
        assertEquals("mid", recipient.getMessageId());
        assertNotNull(recipient.toString());
    }

    /**
     * Проверяет корректность equals и hashCode.
     */
    @Test
    void givenSameIds_WhenCompared_ThenEqualsAndHashCodeMatch() {
        // Arrange
        MailingRecipient first = MailingRecipient.builder().id(1L).build();
        MailingRecipient second = MailingRecipient.builder().id(1L).build();

        // Act
        boolean equalsResult = first.equals(second);
        int hash1 = first.hashCode();
        int hash2 = second.hashCode();

        // Assert
        assertEquals(true, equalsResult);
        assertEquals(hash1, hash2);
    }
}
