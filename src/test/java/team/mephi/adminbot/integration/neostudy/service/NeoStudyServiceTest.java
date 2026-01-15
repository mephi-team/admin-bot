package team.mephi.adminbot.integration.neostudy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import team.mephi.adminbot.integration.neostudy.client.NeoStudyClient;
import team.mephi.adminbot.integration.neostudy.config.NeoStudyProperties;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserRequest;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyUserResponse;
import team.mephi.adminbot.integration.neostudy.mapper.NeoStudyMapper;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.DirectionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для NeoStudyService.
 * Проверяют реакции на ответы NeoStudy при регистрации пользователя.
 */
@ExtendWith(MockitoExtension.class)
class NeoStudyServiceTest {
    @Mock
    private NeoStudyClient neoStudyClient;
    @Mock
    private NeoStudyMapper neoStudyMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DirectionRepository directionRepository;
    @Mock
    private NeoStudyProperties properties;

    /**
     * Проверяет пропуск регистрации при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_registerUser_Then_returnsSameUser() {
        // Arrange
        NeoStudyService service = new NeoStudyService(
                neoStudyClient,
                neoStudyMapper,
                userRepository,
                directionRepository,
                properties
        );
        User user = User.builder().id(1L).build();
        when(properties.isEnabled()).thenReturn(false);

        // Act
        User result = service.registerUser(user);

        // Assert
        assertSame(user, result);
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
    }

    /**
     * Проверяет отсутствие сохранения при пустом ответе NeoStudy.
     */
    @Test
    void Given_emptyResponse_When_registerUser_Then_returnsUserWithoutSaving() {
        // Arrange
        NeoStudyService service = new NeoStudyService(
                neoStudyClient,
                neoStudyMapper,
                userRepository,
                directionRepository,
                properties
        );
        User user = User.builder().id(2L).tgId("tg-2").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-2").build();
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.getUserByExternalId(eq("tg-2"))).thenReturn(Mono.empty());

        // Act
        User result = service.registerUser(user);

        // Assert
        assertSame(user, result);
        assertNull(result.getNeostudyExternalId());
        verify(neoStudyClient).getUserByExternalId(eq("tg-2"));
        verifyNoInteractions(userRepository);
    }

    /**
     * Проверяет регистрацию через создание при ошибке поиска пользователя.
     */
    @Test
    void Given_lookupError_When_registerUser_Then_createsAndUpdatesUser() {
        // Arrange
        NeoStudyService service = new NeoStudyService(
                neoStudyClient,
                neoStudyMapper,
                userRepository,
                directionRepository,
                properties
        );
        User user = User.builder().id(3L).tgId("tg-3").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-3").build();
        NeoStudyUserResponse created = NeoStudyUserResponse.builder().id("neo-1").build();
        NeoStudyUserResponse updated = NeoStudyUserResponse.builder().id("neo-2").build();
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.getUserByExternalId(eq("tg-3"))).thenReturn(Mono.error(new RuntimeException("missing")));
        when(neoStudyClient.createUser(eq(request))).thenReturn(Mono.just(created));
        when(neoStudyClient.updateUser(eq("neo-1"), eq(request))).thenReturn(Mono.just(updated));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = service.registerUser(user);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("neo-2", userCaptor.getValue().getNeostudyExternalId());
        assertEquals("neo-2", result.getNeostudyExternalId());
        assertEquals(user.getId(), result.getId());
        assertNotNull(userCaptor.getValue().getNeostudySyncedAt());
        assertEquals(Instant.class, userCaptor.getValue().getNeostudySyncedAt().getClass());
    }

    /**
     * Проверяет обновление пользователя при успешном поиске в NeoStudy.
     */
    @Test
    void Given_existingUser_When_registerUser_Then_updatesAndSavesUser() {
        // Arrange
        NeoStudyService service = new NeoStudyService(
                neoStudyClient,
                neoStudyMapper,
                userRepository,
                directionRepository,
                properties
        );
        User user = User.builder().id(4L).tgId("tg-4").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-4").build();
        NeoStudyUserResponse existing = NeoStudyUserResponse.builder().id("neo-10").build();
        NeoStudyUserResponse updated = NeoStudyUserResponse.builder().id("neo-11").build();
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.getUserByExternalId(eq("tg-4"))).thenReturn(Mono.just(existing));
        when(neoStudyClient.updateUser(eq("neo-10"), eq(request))).thenReturn(Mono.just(updated));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = service.registerUser(user);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        verify(neoStudyClient, never()).createUser(any(NeoStudyUserRequest.class));
        assertEquals("neo-11", result.getNeostudyExternalId());
        assertNotNull(userCaptor.getValue().getNeostudySyncedAt());
    }
}
