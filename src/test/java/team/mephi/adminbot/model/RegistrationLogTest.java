package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.RegistrationAction;
import team.mephi.adminbot.model.enums.RegistrationStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности RegistrationLog (проверка Lombok-методов и полей).
 */
class RegistrationLogTest {

    @Test
    void builder_shouldSetFields() {
        // given
        User user = new User();
        user.setId(1L);

        // when
        RegistrationLog log = RegistrationLog.builder()
                .id(10L)
                .user(user)
                .action(RegistrationAction.REGISTER)
                .status(RegistrationStatus.SUCCESS)
                .payload("{\"key\": \"value\"}")
                .build();

        // then
        assertEquals(10L, log.getId());
        assertSame(user, log.getUser());
        assertEquals(RegistrationAction.REGISTER, log.getAction());
        assertEquals(RegistrationStatus.SUCCESS, log.getStatus());
        assertEquals("{\"key\": \"value\"}", log.getPayload());
        assertNotNull(log.toString());
    }

    @Test
    void equalsHashCode_shouldWorkBasedOnId() {
        // given
        RegistrationLog a = RegistrationLog.builder().id(1L).build();
        RegistrationLog b = RegistrationLog.builder().id(1L).build();
        RegistrationLog c = RegistrationLog.builder().id(2L).build();

        // when / then
        assertEquals(a, b, "Объекты с одинаковым id должны быть равны");
        assertEquals(a.hashCode(), b.hashCode(), "Хеш-коды объектов с одинаковым id должны совпадать");
        assertNotEquals(a, c, "Объекты с разными id не должны быть равны");
    }

    @Test
    void builder_shouldSupportAllRegistrationActions() {
        // given / when / then
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .action(RegistrationAction.REGISTER)
                .build());
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .action(RegistrationAction.IDP_PENDING)
                .build());
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .action(RegistrationAction.SCRIPT_STARTED)
                .build());
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .action(RegistrationAction.SCRIPT_FINISHED)
                .build());
    }

    @Test
    void builder_shouldSupportAllRegistrationStatuses() {
        // given / when / then
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .status(RegistrationStatus.SUCCESS)
                .build());
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .status(RegistrationStatus.FAILED)
                .build());
        assertDoesNotThrow(() -> RegistrationLog.builder()
                .status(RegistrationStatus.IN_PROGRESS)
                .build());
    }
}
