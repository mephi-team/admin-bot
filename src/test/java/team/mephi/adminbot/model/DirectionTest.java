package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link Direction}.
 */
class DirectionTest {

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenDirectionBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        Direction direction = Direction.builder()
                .id(10L)
                .code("IT")
                .name("Информационные технологии")
                .build();

        // Act
        Long id = direction.getId();
        String code = direction.getCode();
        String name = direction.getName();

        // Assert
        assertEquals(10L, id);
        assertEquals("IT", code);
        assertEquals("Информационные технологии", name);
    }

    /**
     * Проверяет работу сеттеров и геттеров.
     */
    @Test
    void givenDirection_WhenSettersCalled_ThenGettersReturnValues() {
        // Arrange
        Direction direction = new Direction();

        // Act
        direction.setId(20L);
        direction.setCode("HR");
        direction.setName("Кадры");

        // Assert
        assertEquals(20L, direction.getId());
        assertEquals("HR", direction.getCode());
        assertEquals("Кадры", direction.getName());
    }
}
