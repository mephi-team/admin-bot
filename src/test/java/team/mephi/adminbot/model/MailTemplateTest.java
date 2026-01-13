package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тесты для сущности {@link MailTemplate}.
 */
class MailTemplateTest {

    /**
     * Проверяет, что одинаковые идентификаторы дают равенство.
     */
    @Test
    void givenSameIds_WhenCompared_ThenEqualsReturnsTrue() {
        // Arrange
        MailTemplate template1 = MailTemplate.builder()
                .id(1L)
                .name("Template 1")
                .subject("Subject 1")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(1L)
                .name("Template 2")
                .subject("Subject 2")
                .build();

        // Act
        boolean equalsResult = template1.equals(template2);

        // Assert
        assertEquals(true, equalsResult, "Шаблоны с одинаковым ID должны быть равны");
        assertEquals(template1.hashCode(), template2.hashCode(), "HashCode должен совпадать для одинаковых ID");
    }

    /**
     * Проверяет, что разные идентификаторы не равны.
     */
    @Test
    void givenDifferentIds_WhenCompared_ThenEqualsReturnsFalse() {
        // Arrange
        MailTemplate template1 = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(2L)
                .name("Template")
                .subject("Subject")
                .build();

        // Act
        boolean equalsResult = template1.equals(template2);

        // Assert
        assertNotEquals(true, equalsResult, "Шаблоны с разными ID не должны быть равны");
    }

    /**
     * Проверяет, что null-идентификаторы не считаются равными.
     */
    @Test
    void givenNullIds_WhenCompared_ThenEqualsReturnsFalse() {
        // Arrange
        MailTemplate template1 = MailTemplate.builder()
                .id(null)
                .name("Template")
                .subject("Subject")
                .build();

        MailTemplate template2 = MailTemplate.builder()
                .id(null)
                .name("Template")
                .subject("Subject")
                .build();

        // Act
        boolean equalsResult = template1.equals(template2);

        // Assert
        assertNotEquals(true, equalsResult, "Шаблоны с null ID не должны быть равны");
    }

    /**
     * Проверяет, что объект равен самому себе.
     */
    @Test
    void givenSameInstance_WhenCompared_ThenEqualsReturnsTrue() {
        // Arrange
        MailTemplate template = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();

        // Act
        boolean equalsResult = template.equals(template);

        // Assert
        assertEquals(true, equalsResult, "Объект должен быть равен самому себе");
    }

    /**
     * Проверяет, что объект не равен объекту другого класса.
     */
    @Test
    void givenDifferentClass_WhenCompared_ThenEqualsReturnsFalse() {
        // Arrange
        MailTemplate template = MailTemplate.builder()
                .id(1L)
                .name("Template")
                .subject("Subject")
                .build();
        Object other = new Object();

        // Act
        boolean equalsResult = template.equals(other);

        // Assert
        assertNotEquals(true, equalsResult, "Шаблон не должен быть равен объекту другого класса");
    }
}
