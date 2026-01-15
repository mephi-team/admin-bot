package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сущности {@link Expert}.
 */
class ExpertTest {

    /**
     * Проверяет значение флага активности по умолчанию.
     */
    @Test
    void givenNewExpert_WhenCreated_ThenIsActiveDefaultsToTrue() {
        // Arrange
        Expert expert = new Expert();

        // Act
        Boolean isActive = expert.getIsActive();

        // Assert
        assertNotNull(isActive, "isActive не должен быть null");
        assertTrue(isActive, "isActive по умолчанию должен быть true");
    }

    /**
     * Проверяет установку флага активности через билдер.
     */
    @Test
    void givenExpertBuilder_WhenIsActiveSet_ThenValuePersists() {
        // Arrange
        Expert expert = Expert.builder()
                .id(10L)
                .isActive(false)
                .build();

        // Act
        Boolean isActive = expert.getIsActive();

        // Assert
        assertEquals(10L, expert.getId());
        assertFalse(isActive, "isActive должен устанавливаться через builder");
    }

    /**
     * Проверяет корректность equals/hashCode/toString.
     */
    @Test
    void givenExpertsWithSameId_WhenCompared_ThenEqualityAndHashCodeMatch() {
        // Arrange
        Expert expert1 = Expert.builder().id(1L).isActive(true).build();
        Expert expert2 = Expert.builder().id(1L).isActive(true).build();

        // Act
        boolean equalsResult = expert1.equals(expert2);
        int hash1 = expert1.hashCode();
        int hash2 = expert2.hashCode();
        String stringValue = expert1.toString();

        // Assert
        assertEquals(true, equalsResult);
        assertEquals(hash1, hash2);
        assertNotNull(stringValue);
    }
}
