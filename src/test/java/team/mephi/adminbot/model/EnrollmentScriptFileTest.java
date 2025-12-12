package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности EnrollmentScriptFile (проверка @PrePersist onCreate).
 */
class EnrollmentScriptFileTest {

    @Test
    void onCreate_shouldSetUploadedAtIfNull() {
        // given
        EnrollmentScriptFile file = EnrollmentScriptFile.builder().build();

        assertNull(file.getUploadedAt(), "До onCreate uploadedAt должен быть null");

        // when
        file.onCreate();

        // then
        assertNotNull(file.getUploadedAt(), "После onCreate uploadedAt должен быть установлен");
    }

    @Test
    void onCreate_shouldNotOverrideUploadedAtIfAlreadySet() {
        // given
        LocalDateTime oldTime = LocalDateTime.now().minusDays(1);
        EnrollmentScriptFile file = EnrollmentScriptFile.builder()
                .uploadedAt(oldTime)
                .build();

        // when
        file.onCreate();

        // then
        assertEquals(oldTime, file.getUploadedAt(), "onCreate не должен перезаписывать uploadedAt, если он уже задан");
    }
}
