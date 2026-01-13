package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Expert;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.ExpertRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link ExpertServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class ExpertServiceImplTest {
    @Mock
    private ExpertRepository expertRepository;

    private ExpertServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ExpertServiceImpl(expertRepository);
    }

    /**
     * Проверяет маппинг экспертов в DTO при поиске.
     */
    @Test
    void givenExperts_WhenFindAllByRoleAndNameCalled_ThenMapsToSimpleUser() {
        // Arrange
        Expert expert = Expert.builder()
                .id(22L)
                .firstName("Elena")
                .lastName("Smirnova")
                .userName("Smirnova Elena")
                .role(Role.builder().code("EXPERT").build())
                .status(UserStatus.ACTIVE)
                .directions(Set.of(Direction.builder().id(9L).name("Backend").build()))
                .build();

        when(expertRepository.findAllByRoleAndName("EXPERT", "Elena", null))
                .thenReturn(List.of(expert));

        // Act
        List<SimpleUser> result = service.findAllByRoleAndName("EXPERT", "Elena", null)
                .collect(Collectors.toList());

        // Assert
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(SimpleUser::getId, SimpleUser::getFullName, SimpleUser::getStatus)
                .containsExactly(22L, "Smirnova Elena", "ACTIVE");
    }
}
