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

@ExtendWith(MockitoExtension.class)
class DirectionServiceImplTest {
    @Mock
    private DirectionRepository directionRepository;

    private DirectionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DirectionServiceImpl(directionRepository);
    }

    @Test
    void getAllDirectionsInitializesOnce() {
        when(directionRepository.findAll()).thenReturn(List.of(Direction.builder().id(1L).name("Analytics").build()));

        List<SimpleDirection> result = service.getAllDirections();

        assertThat(result)
                .extracting(SimpleDirection::getName)
                .contains("Все", "Analytics");
    }

    @Test
    void getByIdMapsDirection() {
        Direction direction = Direction.builder().id(5L).name("DevOps").build();
        when(directionRepository.findById(5L)).thenReturn(Optional.of(direction));

        Optional<SimpleDirection> result = service.getById(5L);

        assertThat(result)
                .isPresent()
                .get()
                .extracting(SimpleDirection::getId, SimpleDirection::getName)
                .containsExactly(5L, "DevOps");
    }
}
