package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сущности {@link ExpertDirection}.
 */
class ExpertDirectionTest {

    /**
     * Проверяет корректность equals/hashCode у составного ключа.
     */
    @Test
    void givenCompositeId_WhenCompared_ThenEqualsAndHashCodeWork() {
        // Arrange
        ExpertDirection.ExpertDirectionId id1 = new ExpertDirection.ExpertDirectionId(1L, 2L);
        ExpertDirection.ExpertDirectionId id2 = new ExpertDirection.ExpertDirectionId(1L, 2L);
        ExpertDirection.ExpertDirectionId id3 = new ExpertDirection.ExpertDirectionId(1L, 3L);

        // Act
        boolean equalSame = id1.equals(id2);
        boolean equalDifferent = id1.equals(id3);

        // Assert
        assertEquals(true, equalSame);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(true, equalDifferent);
        assertNotNull(id1.toString());
    }

    /**
     * Проверяет заполнение идентификатора через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenCompositeIdIsSet() {
        // Arrange
        ExpertDirection.ExpertDirectionId id = new ExpertDirection.ExpertDirectionId(10L, 20L);

        // Act
        ExpertDirection expertDirection = ExpertDirection.builder()
                .id(id)
                .build();

        // Assert
        assertNotNull(expertDirection.getId());
        assertEquals(10L, expertDirection.getId().getExpertId());
        assertEquals(20L, expertDirection.getId().getDirectionId());
        assertNotNull(expertDirection.toString());
    }
}
