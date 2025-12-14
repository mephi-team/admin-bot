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
                .code("admin")
                .name("ADMIN")
                .description("Администратор системы")
                .build();

        // then
        assertEquals("admin", role.getCode());
        assertEquals("ADMIN", role.getName());
        assertEquals("Администратор системы", role.getDescription());
    }

    @Test
    void equals_shouldBeBasedOnCode() {
        // given
        Role role1 = Role.builder()
                .code("student")
                .name("Student")
                .description("Студент")
                .build();

        Role role2 = Role.builder()
                .code("student")
                .name("Student Role")
                .description("Другое описание")
                .build();

        Role role3 = Role.builder()
                .code("admin")
                .name("Admin")
                .description("Администратор")
                .build();

        // then
        assertEquals(role1, role2); // одинаковый code
        assertNotEquals(role1, role3); // разный code
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}