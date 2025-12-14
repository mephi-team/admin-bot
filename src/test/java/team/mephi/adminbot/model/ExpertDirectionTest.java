package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности ExpertDirection (проверка композитного ключа и Lombok-методов).
 */
class ExpertDirectionTest {

    @Test
    void expertDirectionId_shouldSupportEqualsAndHashCode() {
        // given
        ExpertDirection.ExpertDirectionId id1 = new ExpertDirection.ExpertDirectionId(1L, 2L);
        ExpertDirection.ExpertDirectionId id2 = new ExpertDirection.ExpertDirectionId(1L, 2L);
        ExpertDirection.ExpertDirectionId id3 = new ExpertDirection.ExpertDirectionId(1L, 3L);

        // when / then
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertNotNull(id1.toString());
    }

    @Test
    void builder_shouldSetIds() {
        // given / when
        ExpertDirection.ExpertDirectionId id = new ExpertDirection.ExpertDirectionId(10L, 20L);
        ExpertDirection ed = ExpertDirection.builder()
                .id(id)
                .build();

        // then
        assertNotNull(ed.getId());
        assertEquals(10L, ed.getId().getExpertId());
        assertEquals(20L, ed.getId().getDirectionId());
        assertNotNull(ed.toString());
    }
}
