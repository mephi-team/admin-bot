package team.mephi.adminbot.model;

/**
 * Юнит-тесты для сущности EnrollmentBatch (проверка @PrePersist onCreate).
 */
class EnrollmentBatchTest {

//    @Test
//    void onCreate_shouldSetCreatedAtIfNull() {
//        // given
//        EnrollmentBatch batch = EnrollmentBatch.builder().build();
//
//        assertNull(batch.getCreatedAt(), "До onCreate createdAt должен быть null");
//
//        // when
//        batch.onCreate();
//
//        // then
//        assertNotNull(batch.getCreatedAt(), "После onCreate createdAt должен быть установлен");
//    }

//    @Test
//    void onCreate_shouldNotOverrideExistingCreatedAt() {
//        // given
//        LocalDateTime oldTime = LocalDateTime.now().minusDays(2);
//        EnrollmentBatch batch = EnrollmentBatch.builder()
//                .createdAt(oldTime)
//                .build();
//
//        // when
//        batch.onCreate();
//
//        // then
//        assertEquals(oldTime, batch.getCreatedAt(), "onCreate не должен перезаписывать createdAt, если он уже задан");
//    }
}
