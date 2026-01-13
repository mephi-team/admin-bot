package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link UserServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorRepository tutorRepository;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository, tutorRepository);
    }

    /**
     * Проверяет маппинг пользователей в DTO.
     */
    @Test
    void givenUsers_WhenGetAllUsersCalled_ThenMappedToDtos() {
        // Arrange
        User user = User.builder()
                .id(7L)
                .userName("Test User")
                .tgId("@tg")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<UserDto> result = service.getAllUsers();

        // Assert
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(UserDto::getId, UserDto::getUserName, UserDto::getTgName)
                .containsExactly(7L, "Test User", "@tg");
    }

    /**
     * Проверяет получение пользователя по идентификатору.
     */
    @Test
    void givenUserId_WhenGetByIdCalled_ThenOptionalReturned() {
        // Arrange
        User user = User.builder()
                .id(42L)
                .userName("Alice")
                .build();
        when(userRepository.findById(42L)).thenReturn(Optional.of(user));

        // Act
        Optional<UserDto> result = service.getById(42L);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .extracting(UserDto::getId, UserDto::getUserName)
                .containsExactly(42L, "Alice");
    }
}
