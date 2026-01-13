package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.RegistrationAction;
import team.mephi.adminbot.model.enums.RegistrationStatus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Тесты для сущности {@link RegistrationLog}.
 */
class RegistrationLogTest {

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        User user = new User();
        user.setId(1L);

        // Act
        RegistrationLog log = RegistrationLog.builder()
                .id(10L)
                .user(user)
                .action(RegistrationAction.REGISTER)
                .status(RegistrationStatus.SUCCESS)
                .payload("{\"key\": \"value\"}")
                .build();

        // Assert
        assertEquals(10L, log.getId());
        assertSame(user, log.getUser());
        assertEquals(RegistrationAction.REGISTER, log.getAction());
        assertEquals(RegistrationStatus.SUCCESS, log.getStatus());
        assertEquals("{\"key\": \"value\"}", log.getPayload());
        assertNotNull(log.toString());
    }

    /**
     * Проверяет equals/hashCode на основе идентификатора.
     */
    @Test
    void givenSameIds_WhenCompared_ThenEqualsAndHashCodeMatch() {
        // Arrange
        RegistrationLog first = RegistrationLog.builder().id(1L).build();
        RegistrationLog second = RegistrationLog.builder().id(1L).build();
        RegistrationLog third = RegistrationLog.builder().id(2L).build();

        // Act
        boolean equalsSame = first.equals(second);
        boolean equalsDifferent = first.equals(third);

        // Assert
        assertEquals(true, equalsSame, "Объекты с одинаковым id должны быть равны");
        assertEquals(first.hashCode(), second.hashCode(), "Хеш-коды объектов с одинаковым id должны совпадать");
        assertNotEquals(true, equalsDifferent, "Объекты с разными id не должны быть равны");
    }

    /**
     * Проверяет поддержку всех действий регистрации в билдере.
     */
    @Test
    void givenActions_WhenBuilding_ThenNoExceptions() {
        // Arrange
        RegistrationAction[] actions = {
                RegistrationAction.REGISTER,
                RegistrationAction.IDP_PENDING,
                RegistrationAction.SCRIPT_STARTED,
                RegistrationAction.SCRIPT_FINISHED
        };

        // Act
        for (RegistrationAction action : actions) {
            // Assert
            assertDoesNotThrow(() -> RegistrationLog.builder().action(action).build());
        }
    }

    /**
     * Проверяет поддержку всех статусов регистрации в билдере.
     */
    @Test
    void givenStatuses_WhenBuilding_ThenNoExceptions() {
        // Arrange
        RegistrationStatus[] statuses = {
                RegistrationStatus.SUCCESS,
                RegistrationStatus.FAILED,
                RegistrationStatus.IN_PROGRESS
        };

        // Act
        for (RegistrationStatus status : statuses) {
            // Assert
            assertDoesNotThrow(() -> RegistrationLog.builder().status(status).build());
        }
    }
}
