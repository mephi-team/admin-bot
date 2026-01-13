package team.mephi.adminbot.model;

/**
 * Юнит-тесты для сущности Message (проверка дефолтных временных полей и @PreUpdate).
 */
class MessageTest {

//    @Test
//    void newMessage_shouldHaveDefaultTimestamps() {
//        // given / when
//        Message message = new Message();
//
//        // then
//        assertNotNull(message.getCreatedAt(), "createdAt должен быть инициализирован по умолчанию");
//        assertNotNull(message.getUpdatedAt(), "updatedAt должен быть инициализирован по умолчанию");
//
//        assertTrue(
//                Duration.between(message.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
//                "createdAt должен быть примерно текущим временем"
//        );
//        assertTrue(
//                Duration.between(message.getUpdatedAt(), LocalDateTime.now()).getSeconds() < 5,
//                "updatedAt должен быть примерно текущим временем"
//        );
//    }

//    @Test
//    void setUpdatedAt_shouldOverrideDefaultValue() {
//        // given
//        Message message = new Message();
//        LocalDateTime newUpdatedAt = LocalDateTime.now().minusMinutes(10);
//
//        // when
//        message.setUpdatedAt(newUpdatedAt);
//
//        // then
//        assertEquals(newUpdatedAt, message.getUpdatedAt(), "updatedAt должен обновляться сеттером");
//    }

//    @Test
//    void onUpdate_shouldRefreshUpdatedAt() throws InterruptedException {
//        // given
//        Message message = new Message();
//        LocalDateTime before = message.getUpdatedAt();
//
//        Thread.sleep(50);
//
//        // when
//        message.onUpdate();
//
//        // then
//        assertTrue(message.getUpdatedAt().isAfter(before), "updatedAt должен обновиться при onUpdate");
//    }
}
