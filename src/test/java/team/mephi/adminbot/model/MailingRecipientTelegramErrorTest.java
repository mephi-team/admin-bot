package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingChannel;
import team.mephi.adminbot.model.enums.MailingRecipientStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MailingRecipient entity Telegram error handling.
 * <p>
 * These tests verify that the MailingRecipient entity properly supports error tracking
 * for Telegram broadcast failures, specifically when sending to multiple recipients.
 * <p>
 * Covered scenarios:
 * - Setting FAILED status with statusReason for 400 errors
 * - Tracking Telegram-specific delivery errors
 * - Handling various Telegram API error responses
 * - Distinguishing between successful and failed deliveries
 * <p>
 * This documents the contract for broadcast error handling where some recipients
 * may succeed while others fail with Telegram 400 errors.
 */
class MailingRecipientTelegramErrorTest {

    @Test
    void mailingRecipient_shouldSupportFailedStatus() {
        // Given: a mailing recipient that failed
        MailingRecipient recipient = new MailingRecipient();

        // When: setting failed status
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setChannel(MailingChannel.TELEGRAM);

        // Then: status should be FAILED
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertEquals(MailingChannel.TELEGRAM, recipient.getChannel());
    }

    @Test
    void mailingRecipient_shouldSupportStatusReason() {
        // Given: a recipient with delivery error
        MailingRecipient recipient = new MailingRecipient();
        String errorReason = "Telegram API returned 400: Invalid user ID";

        // When: setting status with reason
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatusReason(errorReason);

        // Then: error details should be preserved
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertEquals(errorReason, recipient.getStatusReason());
    }

    @Test
    void mailingRecipient_shouldHandleTelegram400BadRequest() {
        // Given: a Telegram broadcast that received 400 for this recipient
        MailingRecipient recipient = new MailingRecipient();

        // When: recording the error
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: user not found");
        recipient.setMessageId(null); // No message ID because send failed

        // Then: error should be captured
        assertEquals(MailingChannel.TELEGRAM, recipient.getChannel());
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertNotNull(recipient.getStatusReason());
        assertTrue(recipient.getStatusReason().contains("400"));
        assertNull(recipient.getMessageId());
    }

    @Test
    void mailingRecipient_shouldHandleBotBlockedByUser() {
        // Given: a recipient who blocked the bot
        MailingRecipient recipient = new MailingRecipient();

        // When: recording the blocking
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: bot was blocked by the user");

        // Then: should capture blocking information
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertTrue(recipient.getStatusReason().contains("blocked"));
    }

    @Test
    void mailingRecipient_shouldHandleInvalidChatId() {
        // Given: a recipient with invalid chat ID
        MailingRecipient recipient = new MailingRecipient();

        // When: recording invalid chat error
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: chat_id is invalid");

        // Then: should capture the specific error
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertTrue(recipient.getStatusReason().contains("chat_id"));
    }

    @Test
    void mailingRecipient_shouldHandleUserDeactivated() {
        // Given: a recipient whose account was deactivated
        MailingRecipient recipient = new MailingRecipient();

        // When: recording deactivation error
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: user is deactivated");

        // Then: should capture deactivation
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertTrue(recipient.getStatusReason().contains("deactivated"));
    }

    @Test
    void mailingRecipient_shouldTrackSuccessfulDelivery() {
        // Given: a successfully delivered message
        MailingRecipient recipient = new MailingRecipient();

        // When: recording successful delivery
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.DELIVERED);
        recipient.setMessageId("msg_12345");
        recipient.setStatusReason(null);

        // Then: should show success
        assertEquals(MailingRecipientStatus.DELIVERED, recipient.getStatus());
        assertNotNull(recipient.getMessageId());
        assertNull(recipient.getStatusReason());
    }

    @Test
    void mailingRecipient_shouldDistinguishSuccessFromFailure() {
        // Given: two recipients in a broadcast
        MailingRecipient successRecipient = new MailingRecipient();
        MailingRecipient failedRecipient = new MailingRecipient();

        // When: one succeeds, one fails
        successRecipient.setChannel(MailingChannel.TELEGRAM);
        successRecipient.setStatus(MailingRecipientStatus.DELIVERED);
        successRecipient.setMessageId("msg_123");

        failedRecipient.setChannel(MailingChannel.TELEGRAM);
        failedRecipient.setStatus(MailingRecipientStatus.FAILED);
        failedRecipient.setStatusReason("400 Bad Request: bot blocked");

        // Then: should be clearly distinguishable
        assertNotEquals(successRecipient.getStatus(), failedRecipient.getStatus());
        assertNotNull(successRecipient.getMessageId());
        assertNull(failedRecipient.getMessageId());
        assertNull(successRecipient.getStatusReason());
        assertNotNull(failedRecipient.getStatusReason());
    }

    @Test
    void mailingRecipient_shouldSupportDifferentChannels() {
        // Given: recipients on different channels
        MailingRecipient telegramRecipient = new MailingRecipient();
        MailingRecipient emailRecipient = new MailingRecipient();

        // When: setting different channels
        telegramRecipient.setChannel(MailingChannel.TELEGRAM);
        telegramRecipient.setStatus(MailingRecipientStatus.FAILED);
        telegramRecipient.setStatusReason("400 Bad Request: Telegram error");

        emailRecipient.setChannel(MailingChannel.EMAIL);
        emailRecipient.setStatus(MailingRecipientStatus.FAILED);
        emailRecipient.setStatusReason("SMTP error");

        // Then: should support channel-specific errors
        assertEquals(MailingChannel.TELEGRAM, telegramRecipient.getChannel());
        assertEquals(MailingChannel.EMAIL, emailRecipient.getChannel());
        assertNotEquals(telegramRecipient.getChannel(), emailRecipient.getChannel());
    }

    @Test
    void mailingRecipient_shouldHandlePendingStatus() {
        // Given: a recipient waiting to be processed
        MailingRecipient recipient = new MailingRecipient();

        // When: initial state
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.PENDING);
        recipient.setStatusReason(null);

        // Then: should be pending without error
        assertEquals(MailingRecipientStatus.PENDING, recipient.getStatus());
        assertNull(recipient.getStatusReason());
    }

    @Test
    void mailingRecipient_statusReason_shouldSupportLongErrors() {
        // Given: a detailed error message
        MailingRecipient recipient = new MailingRecipient();
        String longError = "Telegram API Error 400: " +
                "The message text contains forbidden content and cannot be sent. " +
                "Please review the content and try again with appropriate modifications.";

        // When: setting long error
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason(longError);

        // Then: long error should be preserved
        assertEquals(longError, recipient.getStatusReason());
        assertTrue(recipient.getStatusReason().length() > 100);
    }

    @Test
    void mailingRecipient_shouldHandleMessageTooLongError() {
        // Given: a message that exceeds Telegram limits
        MailingRecipient recipient = new MailingRecipient();

        // When: recording message too long error
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: message is too long");

        // Then: should capture the validation error
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertTrue(recipient.getStatusReason().contains("too long"));
    }

    @Test
    void mailingRecipient_shouldHandleRateLimitError() {
        // Given: hitting Telegram rate limits
        MailingRecipient recipient = new MailingRecipient();

        // When: recording rate limit (often comes as 429, but can be 400 with specific message)
        recipient.setChannel(MailingChannel.TELEGRAM);
        recipient.setStatus(MailingRecipientStatus.FAILED);
        recipient.setStatusReason("400 Bad Request: Too many requests");

        // Then: should capture rate limiting
        assertEquals(MailingRecipientStatus.FAILED, recipient.getStatus());
        assertTrue(recipient.getStatusReason().toLowerCase().contains("too many"));
    }

    /**
     * Documentation test: demonstrates the expected contract for handling
     * Telegram API 400 errors in broadcast scenarios via MailingRecipient entity.
     * <p>
     * When a broadcast to multiple recipients encounters Telegram 400 errors:
     * 1. Each recipient gets their own MailingRecipient record
     * 2. Failed recipients: status = FAILED, statusReason = error details, messageId = null
     * 3. Successful recipients: status = DELIVERED/SENT, messageId = telegram msg ID
     * 4. Channel should be set to TELEGRAM
     * 5. sentAt/readAt timestamps as appropriate
     * <p>
     * This allows tracking which specific users failed and why in a broadcast.
     */
    @Test
    void telegram400Error_broadcastContractTest() {
        // Given: a broadcast to 3 recipients, 2 succeed, 1 fails with 400
        MailingRecipient success1 = MailingRecipient.builder()
                .channel(MailingChannel.TELEGRAM)
                .status(MailingRecipientStatus.DELIVERED)
                .messageId("telegram_msg_001")
                .statusReason(null)
                .build();

        MailingRecipient success2 = MailingRecipient.builder()
                .channel(MailingChannel.TELEGRAM)
                .status(MailingRecipientStatus.DELIVERED)
                .messageId("telegram_msg_002")
                .statusReason(null)
                .build();

        MailingRecipient failed = MailingRecipient.builder()
                .channel(MailingChannel.TELEGRAM)
                .status(MailingRecipientStatus.FAILED)
                .statusReason("400 Bad Request: bot was blocked by the user")
                .messageId(null)
                .build();

        // Then: verify the contract
        // Successful deliveries
        assertEquals(MailingRecipientStatus.DELIVERED, success1.getStatus());
        assertNotNull(success1.getMessageId());
        assertNull(success1.getStatusReason());

        assertEquals(MailingRecipientStatus.DELIVERED, success2.getStatus());
        assertNotNull(success2.getMessageId());
        assertNull(success2.getStatusReason());

        // Failed delivery
        assertEquals(MailingRecipientStatus.FAILED, failed.getStatus());
        assertNull(failed.getMessageId());
        assertNotNull(failed.getStatusReason());
        assertTrue(failed.getStatusReason().contains("400"));

        // All should be Telegram channel
        assertEquals(MailingChannel.TELEGRAM, success1.getChannel());
        assertEquals(MailingChannel.TELEGRAM, success2.getChannel());
        assertEquals(MailingChannel.TELEGRAM, failed.getChannel());

        // This documents that:
        // - The MailingRecipient entity supports per-recipient error tracking
        // - Broadcasts can have mixed success/failure states
        // - Each failure reason is preserved for debugging
        // - Services should implement this pattern for broadcast error handling
    }
}
