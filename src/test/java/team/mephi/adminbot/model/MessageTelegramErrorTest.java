package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MessageStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Message entity error handling scenarios.
 * 
 * These tests verify that the Message entity properly supports error tracking
 * for Telegram integration failures, specifically:
 * - MessageStatus.FAILED for error states
 * - statusReason field for error descriptions
 * - telegramMessageId for external system correlation
 * 
 * This documents the contract for how Telegram 400 errors should be handled:
 * when a message fails to send (e.g., 400 Bad Request from Telegram API),
 * the Message entity should track this with FAILED status and descriptive reason.
 */
class MessageTelegramErrorTest {

    @Test
    void message_shouldSupportFailedStatus() {
        // Given: a message that failed to send
        Message message = new Message();
        
        // When: setting failed status
        message.setStatus(MessageStatus.FAILED);
        
        // Then: status should be FAILED
        assertEquals(MessageStatus.FAILED, message.getStatus());
    }

    @Test
    void message_shouldSupportStatusReason() {
        // Given: a message with error details
        Message message = new Message();
        String errorReason = "Telegram API returned 400: Invalid chat_id";
        
        // When: setting status reason
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason(errorReason);
        
        // Then: status reason should be preserved
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertEquals(errorReason, message.getStatusReason());
    }

    @Test
    void message_shouldHandleTelegram400BadRequest() {
        // Given: a Telegram message that received 400 Bad Request
        Message message = new Message();
        
        // When: recording the error
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: chat not found");
        message.setTelegramMessageId(null); // No message ID because send failed
        
        // Then: error details should be captured
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertNotNull(message.getStatusReason());
        assertTrue(message.getStatusReason().contains("400"));
        assertNull(message.getTelegramMessageId());
    }

    @Test
    void message_shouldHandleTelegramInvalidChatId() {
        // Given: a message with invalid chat ID error
        Message message = new Message();
        
        // When: recording the specific error
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: chat_id is invalid");
        
        // Then: should capture the specific error
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertTrue(message.getStatusReason().contains("chat_id"));
        assertTrue(message.getStatusReason().contains("invalid"));
    }

    @Test
    void message_shouldHandleTelegramBlockedByUser() {
        // Given: a message blocked by user (common 400 scenario)
        Message message = new Message();
        
        // When: recording blocked status
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: bot was blocked by the user");
        
        // Then: should capture blocking information
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertTrue(message.getStatusReason().contains("blocked"));
    }

    @Test
    void message_shouldHandleTelegramInvalidMessageFormat() {
        // Given: a message with format validation error
        Message message = new Message();
        
        // When: recording format error
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: message text is empty");
        
        // Then: should capture validation error
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertTrue(message.getStatusReason().contains("message text"));
    }

    @Test
    void message_statusReason_canBeNull() {
        // Given: a message in non-error state
        Message message = new Message();
        
        // When: setting status without reason
        message.setStatus(MessageStatus.SENT);
        message.setStatusReason(null);
        
        // Then: null status reason should be allowed for success cases
        assertEquals(MessageStatus.SENT, message.getStatus());
        assertNull(message.getStatusReason());
    }

    @Test
    void message_statusReason_shouldSupportLongErrorMessages() {
        // Given: a message with detailed error
        Message message = new Message();
        String longError = "Telegram API Error 400 Bad Request: Failed to send message. " +
                          "The message contains content that violates Telegram's Terms of Service. " +
                          "Please review the message content and ensure it complies with platform policies " +
                          "before attempting to send again. Error code: CONTENT_POLICY_VIOLATION";
        
        // When: setting long error message
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason(longError);
        
        // Then: long error should be preserved
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertEquals(longError, message.getStatusReason());
        assertTrue(message.getStatusReason().length() > 100);
    }

    @Test
    void message_shouldDistinguishBetweenErrorStates() {
        // Given: different message states
        Message sentMessage = new Message();
        Message failedMessage = new Message();
        
        // When: setting different states
        sentMessage.setStatus(MessageStatus.SENT);
        sentMessage.setStatusReason(null);
        
        failedMessage.setStatus(MessageStatus.FAILED);
        failedMessage.setStatusReason("400 Bad Request");
        
        // Then: should be distinguishable
        assertNotEquals(sentMessage.getStatus(), failedMessage.getStatus());
        assertNull(sentMessage.getStatusReason());
        assertNotNull(failedMessage.getStatusReason());
    }

    @Test
    void message_shouldSupportTelegramMessageIdTracking() {
        // Given: a successfully sent Telegram message
        Message message = new Message();
        
        // When: message is sent successfully
        message.setStatus(MessageStatus.DELIVERED);
        message.setTelegramMessageId("12345678");
        
        // Then: Telegram message ID should be tracked
        assertEquals(MessageStatus.DELIVERED, message.getStatus());
        assertEquals("12345678", message.getTelegramMessageId());
    }

    @Test
    void message_failedMessage_shouldNotHaveTelegramId() {
        // Given: a message that failed to send
        Message message = new Message();
        
        // When: recording failure before message is sent
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: invalid parameters");
        message.setTelegramMessageId(null);
        
        // Then: should not have telegram ID
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertNull(message.getTelegramMessageId());
        assertNotNull(message.getStatusReason());
    }

    /**
     * Documentation test: demonstrates the expected contract for handling
     * Telegram API 400 errors in the Message entity.
     * 
     * When a Telegram message send fails with 400 Bad Request:
     * 1. Set status to MessageStatus.FAILED
     * 2. Set statusReason with error details (e.g., "400 Bad Request: <reason>")
     * 3. Leave telegramMessageId as null (no message was created)
     * 4. Set createdAt/updatedAt timestamps as appropriate
     * 
     * This test serves as documentation for future service implementations.
     */
    @Test
    void telegram400Error_contractTest() {
        // Given: a Telegram service receives 400 Bad Request
        Message message = new Message();
        
        // When: the service should record the error like this:
        message.setStatus(MessageStatus.FAILED);
        message.setStatusReason("400 Bad Request: chat_id is invalid or bot was blocked");
        message.setTelegramMessageId(null);
        // In real implementation: also set createdAt, updatedAt, dialog, sender, etc.
        
        // Then: verify the contract is met
        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertNotNull(message.getStatusReason());
        assertTrue(message.getStatusReason().startsWith("400"));
        assertNull(message.getTelegramMessageId());
        
        // This documents that:
        // - The Message entity supports error tracking
        // - The fields exist for proper Telegram error handling
        // - Services should implement error handling using these fields
        // - Future tests should verify service-level error handling
    }
}
