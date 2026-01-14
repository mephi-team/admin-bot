package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.ConsentStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link PdConsentLog}.
 */
class PdConsentLogTest {

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        User user = new User();
        user.setId(5L);

        // Act
        PdConsentLog log = PdConsentLog.builder()
                .id(10L)
                .user(user)
                .source("web_form")
                .status(ConsentStatus.GRANTED)
                .build();

        // Assert
        assertEquals(10L, log.getId());
        assertEquals(user, log.getUser());
        assertEquals("web_form", log.getSource());
        assertEquals(ConsentStatus.GRANTED, log.getStatus());
    }
}
