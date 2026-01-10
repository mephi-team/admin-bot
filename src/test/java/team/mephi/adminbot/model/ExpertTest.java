package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Expert (проверка дефолтных значений и Lombok-методов).
 */
class ExpertTest {

    @Test
    void newExpert_shouldHaveIsActiveTrueByDefault() {
        // given / when
        Expert expert = new Expert();

        // then
        assertNotNull(expert.getIsActive(), "isActive не должен быть null");
        assertTrue(expert.getIsActive(), "isActive по умолчанию должен быть true");
    }

    @Test
    void builder_shouldAllowExplicitIsActiveValue() {
        // given / when
        Expert expert = Expert.builder()
                .id(10L)
                .isActive(false)
                .build();

        // then
        assertEquals(10L, expert.getId());
        assertFalse(expert.getIsActive(), "isActive должен устанавливаться через builder");
    }

    @Test
    void equalsHashCodeAndToString_shouldWork() {
        // given
        Expert e1 = Expert.builder().id(1L).isActive(true).build();
        Expert e2 = Expert.builder().id(1L).isActive(true).build();

        // then
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
    }
}
