package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link Mailing}.
 */
class MailingTest {

    /**
     * Проверяет заполнение идентификатора и названия через builder.
     */
    @Test
    void givenBuilder_WhenBuild_ThenIdAndNameAreSet() {
        // Arrange
        Mailing mailing = Mailing.builder()
                .id(100L)
                .name("Mailing name")
                .build();

        // Act
        Long id = mailing.getId();
        String name = mailing.getName();

        // Assert
        assertEquals(100L, id);
        assertEquals("Mailing name", name);
    }
}
