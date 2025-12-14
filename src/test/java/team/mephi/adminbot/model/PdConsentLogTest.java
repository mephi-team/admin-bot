package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности PdConsentLog (проверка @PrePersist onCreate).
 */
class PdConsentLogTest {

    @Test
    void onCreate_shouldSetConsentedAtIfNull() {
        // given
        PdConsentLog log = PdConsentLog.builder().build();

        assertNull(log.getConsentedAt(), "До onCreate consentedAt должен быть null");

        // when
        log.onCreate();

        // then
        assertNotNull(log.getConsentedAt(), "После onCreate consentedAt должен быть установлен");
    }

    @Test
    void onCreate_shouldNotOverrideConsentedAtIfAlreadySet() {
        // given
        LocalDateTime oldTime = LocalDateTime.now().minusHours(6);
        PdConsentLog log = PdConsentLog.builder()
                .consentedAt(oldTime)
                .build();

        // when
        log.onCreate();

        // then
        assertEquals(oldTime, log.getConsentedAt(), "onCreate не должен перезаписывать consentedAt, если он уже задан");
    }
}
