package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тесты для сущности {@link Role}.
 */
class RoleTest {

    /**
     * Проверяет, что билдер корректно заполняет поля роли.
     */
    @Test
    void givenRoleBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        Role role = Role.builder()
                .code("admin")
                .name("ADMIN")
                .description("Администратор системы")
                .build();

        // Act
        String code = role.getCode();
        String name = role.getName();
        String description = role.getDescription();

        // Assert
        assertEquals("admin", code);
        assertEquals("ADMIN", name);
        assertEquals("Администратор системы", description);
    }

    /**
     * Проверяет, что сравнение ролей основано на коде роли.
     */
    @Test
    void givenRolesWithSameCode_WhenCompared_ThenEqualsUsesCode() {
        // Arrange
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

        // Act
        boolean sameCodeEquals = role1.equals(role2);
        boolean differentCodeEquals = role1.equals(role3);

        // Assert
        assertEquals(true, sameCodeEquals);
        assertNotEquals(true, differentCodeEquals);
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}
