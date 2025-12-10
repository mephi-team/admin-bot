package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Direction (проверка геттеров/сеттеров/билдера).
 */
class DirectionTest {

    @Test
    void builder_shouldCreateDirectionWithAllFields() {
        // given / when
        Direction direction = Direction.builder()
                .id(10L)
                .code("IT")
                .name("Информационные технологии")
                .build();

        // then
        assertEquals(10L, direction.getId());
        assertEquals("IT", direction.getCode());
        assertEquals("Информационные технологии", direction.getName());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Direction direction = new Direction();

        // when
        direction.setId(20L);
        direction.setCode("HR");
        direction.setName("Кадры");

        // then
        assertEquals(20L, direction.getId());
        assertEquals("HR", direction.getCode());
        assertEquals("Кадры", direction.getName());
    }
}
