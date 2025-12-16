package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Юнит-тесты для EnrollmentScriptFile.
 * <p>
 * Проверяют логику метода onCreate(),
 * который вызывается перед сохранением сущности:
 * - установка uploadedAt
 * - установка статуса по умолчанию
 */
class EnrollmentScriptFileTest {

//    @Test
//    void onCreate_shouldSetUploadedAtIfNull() {
//        // given: файл без даты загрузки
//        EnrollmentScriptFile file = EnrollmentScriptFile.builder().build();
//        assertNull(file.getUploadedAt(),
//                "До вызова onCreate uploadedAt должен быть null");
//
//        // when: имитируем @PrePersist
//        file.onCreate();
//
//        // then: дата должна быть установлена
//        assertNotNull(file.getUploadedAt(),
//                "После onCreate uploadedAt должен быть установлен");
//    }

//    @Test
//    void onCreate_shouldNotOverrideUploadedAtIfAlreadySet() {
//        // given: файл с заранее заданной датой
//        LocalDateTime existingTime = LocalDateTime.now().minusDays(1);
//        EnrollmentScriptFile file = EnrollmentScriptFile.builder()
//                .uploadedAt(existingTime)
//                .build();
//
//        // when
//        file.onCreate();
//
//        // then: значение не должно измениться
//        assertEquals(existingTime, file.getUploadedAt(),
//                "onCreate не должен перезаписывать uploadedAt, если он уже задан");
//    }

    @Test
    void onCreate_shouldSetDefaultStatusIfNull() {
        // given: файл без статуса
        EnrollmentScriptFile file = EnrollmentScriptFile.builder().build();
        assertNull(file.getStatus(),
                "До вызова onCreate status должен быть null");

        // when
        file.onCreate();

        // then: статус по умолчанию
        assertEquals(ScriptTaskStatus.PENDING, file.getStatus(),
                "После onCreate status должен быть установлен в PENDING");
    }

    @Test
    void onCreate_shouldNotOverrideStatusIfAlreadySet() {
        // given: файл со статусом RUNNING
        EnrollmentScriptFile file = EnrollmentScriptFile.builder()
                .status(ScriptTaskStatus.RUNNING)
                .build();

        // when
        file.onCreate();

        // then: статус не должен измениться
        assertEquals(ScriptTaskStatus.RUNNING, file.getStatus(),
                "onCreate не должен перезаписывать status, если он уже задан");
    }
}
