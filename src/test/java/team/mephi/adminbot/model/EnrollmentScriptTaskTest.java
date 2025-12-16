package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Юнит-тесты для сущности EnrollmentScriptTask (проверка дефолтов и Lombok-методов).
 */
class EnrollmentScriptTaskTest {

    @Test
    void newTask_shouldHaveDefaultCounters() {
        // given / when
        EnrollmentScriptTask task = new EnrollmentScriptTask();

        // then
        assertNotNull(task.getSuccessCount(), "successCount не должен быть null");
        assertNotNull(task.getErrorCount(), "errorCount не должен быть null");
        assertEquals(0, task.getSuccessCount(), "successCount по умолчанию должен быть 0");
        assertEquals(0, task.getErrorCount(), "errorCount по умолчанию должен быть 0");
    }

//    @Test
//    void builder_shouldSetFields() {
//        // given / when
//        EnrollmentScriptTask task = EnrollmentScriptTask.builder()
//                .id(1L)
//                .status(ScriptTaskStatus.PENDING)
//                .log(Map.of("k", "v"))
//                .successCount(5)
//                .errorCount(2)
//                .build();
//
//        // then
//        assertEquals(1L, task.getId());
//        assertEquals(ScriptTaskStatus.PENDING, task.getStatus());
//        assertEquals(5, task.getSuccessCount());
//        assertEquals(2, task.getErrorCount());
//        assertNotNull(task.toString());
//    }
}
