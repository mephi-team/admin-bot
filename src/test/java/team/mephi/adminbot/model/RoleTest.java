package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Role (проверка геттеров/сеттеров/билдера).
 */
class RoleTest {

    @Test
    void builder_shouldCreateRoleWithAllFields() {
        // given / when
        Role role = Role.builder()
                .code(1L)
                .name("ADMIN")
                .description("Администратор системы")
                .build();

        // then
        assertEquals(1L, role.getCode());
        assertEquals("ADMIN", role.getName());
        assertEquals("Администратор системы", role.getDescription());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Role role = new Role();

        // when
        role.setCode(2L);
        role.setName("USER");
        role.setDescription("Обычный пользователь");

        // then
        assertEquals(2L, role.getCode());
        assertEquals("USER", role.getName());
        assertEquals("Обычный пользователь", role.getDescription());
    }
}