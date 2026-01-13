package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.repository.DirectionRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link DirectionServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class DirectionServiceImplTest {
    @Mock
    private DirectionRepository directionRepository;

    private DirectionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DirectionServiceImpl(directionRepository);
    }

    /**
     * Проверяет инициализацию списка направлений при первом запросе.
     */
    @Test
    void givenRepositoryData_WhenGetAllDirectionsCalled_ThenIncludesDefaults() {
        // Arrange
        when(directionRepository.findAll()).thenReturn(List.of(Direction.builder().id(1L).name("Analytics").build()));

        // Act
        List<SimpleDirection> result = service.getAllDirections();

        // Assert
        assertThat(result)
                .extracting(SimpleDirection::getName)
                .contains("Все", "Analytics");
    }

    /**
     * Проверяет маппинг направления по идентификатору.
     */
    @Test
    void givenDirectionId_WhenGetByIdCalled_ThenDirectionMapped() {
        // Arrange
        Direction direction = Direction.builder().id(5L).name("DevOps").build();
        when(directionRepository.findById(5L)).thenReturn(Optional.of(direction));

        // Act
        Optional<SimpleDirection> result = service.getById(5L);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .extracting(SimpleDirection::getId, SimpleDirection::getName)
                .containsExactly(5L, "DevOps");
    }
}
