package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link AdminAudit}.
 */
class AdminAuditTest {

    /**
     * Проверяет, что билдер корректно заполняет основные поля.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        AdminAudit audit = AdminAudit.builder()
                .entityType("USER")
                .entityId(15L)
                .action("CREATE")
                .build();

        // Act
        String entityType = audit.getEntityType();
        Long entityId = audit.getEntityId();
        String action = audit.getAction();

        // Assert
        assertEquals("USER", entityType);
        assertEquals(15L, entityId);
        assertEquals("CREATE", action);
    }
}
