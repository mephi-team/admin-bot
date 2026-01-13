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

    @Test
    void getAllUsersMapsUserDtos() {
        User user = User.builder()
                .id(7L)
                .userName("Test User")
                .tgId("@tg")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = service.getAllUsers();

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(UserDto::getId, UserDto::getUserName, UserDto::getTgName)
                .containsExactly(7L, "Test User", "@tg");
    }

    @Test
    void getByIdReturnsOptional() {
        User user = User.builder()
                .id(42L)
                .userName("Alice")
                .build();
        when(userRepository.findById(42L)).thenReturn(Optional.of(user));

        Optional<UserDto> result = service.getById(42L);

        assertThat(result)
                .isPresent()
                .get()
                .extracting(UserDto::getId, UserDto::getUserName)
                .containsExactly(42L, "Alice");
    }
}
