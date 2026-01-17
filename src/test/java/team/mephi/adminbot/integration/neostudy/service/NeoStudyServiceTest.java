package team.mephi.adminbot.integration.neostudy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import team.mephi.adminbot.integration.neostudy.client.NeoStudyClient;
import team.mephi.adminbot.integration.neostudy.config.NeoStudyProperties;
import team.mephi.adminbot.integration.neostudy.dto.*;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyException;
import team.mephi.adminbot.integration.neostudy.mapper.NeoStudyMapper;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.DirectionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для NeoStudyService.
 * Проверяют ключевые бизнес-ветки сервиса интеграции:
 * регистрацию/синхронизацию пользователя, синхронизацию курсов,
 * создание записей на курс, синхронизацию статуса записи и обработку вебхуков.
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

    private NeoStudyService service;

    @BeforeEach
    void setUp() {
        service = new NeoStudyService(
                neoStudyClient,
                neoStudyMapper,
                userRepository,
                directionRepository,
                properties
        );
    }

    // =========================================================================
    // registerUser
    // =========================================================================

    /**
     * Проверяет пропуск регистрации при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_registerUser_Then_returnsSameUser() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);
        User user = User.builder().id(1L).tgId("tg-1").build();

        // Act
        User result = service.registerUser(user);

        // Assert
        assertSame(user, result);
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(directionRepository);
        verifyNoInteractions(neoStudyMapper);
    }

    /**
     * Проверяет отсутствие сохранения при пустом ответе NeoStudy.
     */
    @Test
    void Given_emptyResponse_When_registerUser_Then_returnsUserWithoutSaving() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(2L).tgId("tg-2").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder()
                .externalId("tg-2")
                .name("U2")
                .status("ACTIVE")
                .build();

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
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(3L).tgId("tg-3").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder()
                .externalId("tg-3")
                .name("U3")
                .status("ACTIVE")
                .build();

        NeoStudyUserResponse created = NeoStudyUserResponse.builder().id("neo-1").build();
        NeoStudyUserResponse updated = NeoStudyUserResponse.builder().id("neo-2").build();

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
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(4L).tgId("tg-4").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder()
                .externalId("tg-4")
                .name("U4")
                .status("ACTIVE")
                .build();

        NeoStudyUserResponse existing = NeoStudyUserResponse.builder().id("neo-10").build();
        NeoStudyUserResponse updated = NeoStudyUserResponse.builder().id("neo-11").build();

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

    /**
     * Проверяет оборачивание неожиданных ошибок в NeoStudyException при регистрации.
     */
    @Test
    void Given_updateUserThrows_When_registerUser_Then_wrapsIntoNeoStudyException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(40L).tgId("tg-40").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-40").build();

        NeoStudyUserResponse existing = NeoStudyUserResponse.builder().id("neo-40").build();

        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.getUserByExternalId(eq("tg-40"))).thenReturn(Mono.just(existing));
        when(neoStudyClient.updateUser(eq("neo-40"), eq(request))).thenReturn(Mono.error(new RuntimeException("boom")));

        // Act + Assert
        NeoStudyException ex = assertThrows(NeoStudyException.class, () -> service.registerUser(user));
        assertTrue(ex.getMessage().contains("Не удалось зарегистрировать пользователя в NeoStudy"));
        verifyNoInteractions(userRepository);
    }

    // =========================================================================
    // syncUser
    // =========================================================================

    /**
     * Проверяет пропуск синхронизации пользователя при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_syncUser_Then_returnsSameUser() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);
        User user = User.builder().id(50L).tgId("tg-50").neostudyExternalId("neo-50").build();

        // Act
        User result = service.syncUser(user);

        // Assert
        assertSame(user, result);
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(neoStudyMapper);
    }

    /**
     * Проверяет, что при отсутствии NeoStudy ID синхронизация делегируется в registerUser().
     */
    @Test
    void Given_userHasNoNeoStudyId_When_syncUser_Then_delegatesToRegisterUser() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyService spyService = spy(service);
        User user = User.builder().id(51L).tgId("tg-51").neostudyExternalId(null).build();

        User registered = User.builder()
                .id(51L)
                .tgId("tg-51")
                .neostudyExternalId("neo-51")
                .neostudySyncedAt(Instant.now())
                .build();

        doReturn(registered).when(spyService).registerUser(eq(user));

        // Act
        User result = spyService.syncUser(user);

        // Assert
        assertSame(registered, result);
        verify(spyService).registerUser(eq(user));
        verifyNoMoreInteractions(neoStudyClient);
    }

    /**
     * Проверяет обновление пользователя через mapper и сохранение при успешном updateUser().
     */
    @Test
    void Given_updateUserReturnsResponse_When_syncUser_Then_updatesViaMapperAndSaves() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder()
                .id(52L)
                .tgId("tg-52")
                .neostudyExternalId("neo-52")
                .userName("Old")
                .build();

        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-52").name("Old").build();
        NeoStudyUserResponse response = NeoStudyUserResponse.builder()
                .id("neo-52")
                .name("New Name")
                .status("active")
                .build();

        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.updateUser(eq("neo-52"), eq(request))).thenReturn(Mono.just(response));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        User result = service.syncUser(user);

        // Assert
        verify(neoStudyMapper).updateUserFromNeoStudy(eq(user), eq(response));
        verify(userRepository).save(eq(user));
        assertNotNull(result.getNeostudySyncedAt());
    }

    /**
     * Проверяет оборачивание ошибок в NeoStudyException при синхронизации пользователя.
     */
    @Test
    void Given_updateUserThrows_When_syncUser_Then_wrapsIntoNeoStudyException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(53L).tgId("tg-53").neostudyExternalId("neo-53").build();
        NeoStudyUserRequest request = NeoStudyUserRequest.builder().externalId("tg-53").build();

        when(neoStudyMapper.toNeoStudyUserRequest(eq(user))).thenReturn(request);
        when(neoStudyClient.updateUser(eq("neo-53"), eq(request))).thenReturn(Mono.error(new RuntimeException("boom")));

        // Act + Assert
        NeoStudyException ex = assertThrows(NeoStudyException.class, () -> service.syncUser(user));
        assertTrue(ex.getMessage().contains("Не удалось синхронизировать пользователя с NeoStudy"));
        verifyNoInteractions(userRepository);
    }

    // =========================================================================
    // syncCourses
    // =========================================================================

    /**
     * Проверяет пропуск синхронизации курсов при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_syncCourses_Then_returnsEmptyList() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);

        // Act
        List<Direction> result = service.syncCourses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(directionRepository);
        verifyNoInteractions(neoStudyMapper);
    }

    /**
     * Проверяет возврат пустого списка, если NeoStudy не вернул курсы.
     */
    @Test
    void Given_emptyCourses_When_syncCourses_Then_returnsEmptyList() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyClient.getCourses()).thenReturn(Mono.just(List.of()));

        // Act
        List<Direction> result = service.syncCourses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(neoStudyClient).getCourses();
        verifyNoInteractions(directionRepository);
        verifyNoInteractions(neoStudyMapper);
    }

    /**
     * Проверяет создание нового направления, если в базе нет направления с таким code.
     */
    @Test
    void Given_noExistingDirection_When_syncCourses_Then_createsAndSavesNewDirection() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyCourseResponse course = NeoStudyCourseResponse.builder()
                .id("course-1")
                .code("CS101")
                .name("Intro")
                .build();

        when(neoStudyClient.getCourses()).thenReturn(Mono.just(List.of(course)));
        when(directionRepository.findAll()).thenReturn(List.of());

        Direction mapped = Direction.builder().code("CS101").name("Intro").build();
        when(neoStudyMapper.toDirection(eq(course))).thenReturn(mapped);

        when(directionRepository.save(any(Direction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<Direction> result = service.syncCourses();

        // Assert
        assertEquals(1, result.size());

        ArgumentCaptor<Direction> directionCaptor = ArgumentCaptor.forClass(Direction.class);
        verify(directionRepository).save(directionCaptor.capture());

        Direction saved = directionCaptor.getValue();
        assertEquals("CS101", saved.getCode());
        assertEquals("Intro", saved.getName());
        assertEquals("course-1", saved.getNeostudyExternalId());
        assertNotNull(saved.getNeostudySyncedAt());
    }

    /**
     * Проверяет обновление существующего направления по совпадающему code.
     */
    @Test
    void Given_existingDirectionByCode_When_syncCourses_Then_updatesAndSavesExistingDirection() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyCourseResponse course = NeoStudyCourseResponse.builder()
                .id("course-2")
                .code("MATH201")
                .name("Algebra")
                .build();

        Direction existing = Direction.builder()
                .id(10L)
                .code("MATH201")
                .name("Old Name")
                .build();

        when(neoStudyClient.getCourses()).thenReturn(Mono.just(List.of(course)));
        when(directionRepository.findAll()).thenReturn(List.of(existing));
        when(directionRepository.save(any(Direction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<Direction> result = service.syncCourses();

        // Assert
        assertEquals(1, result.size());
        verify(neoStudyMapper).updateDirectionFromNeoStudy(eq(existing), eq(course));
        verify(directionRepository).save(eq(existing));

        assertEquals("course-2", existing.getNeostudyExternalId());
        assertNotNull(existing.getNeostudySyncedAt());
    }

    /**
     * Проверяет оборачивание ошибок в NeoStudyException при синхронизации курсов.
     */
    @Test
    void Given_clientThrows_When_syncCourses_Then_wrapsIntoNeoStudyException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyClient.getCourses()).thenReturn(Mono.error(new RuntimeException("boom")));

        // Act + Assert
        NeoStudyException ex = assertThrows(NeoStudyException.class, () -> service.syncCourses());
        assertTrue(ex.getMessage().contains("Не удалось синхронизировать курсы из NeoStudy"));
    }

    // =========================================================================
    // createEnrollment
    // =========================================================================

    /**
     * Проверяет пропуск создания записи при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_createEnrollment_Then_returnsNull() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);

        User user = User.builder().id(60L).tgId("tg-60").build();
        Direction direction = Direction.builder().id(600L).code("D-1").name("Dir").build();

        // Act
        NeoStudyEnrollmentResponse result = service.createEnrollment(user, direction, "active");

        // Assert
        assertNull(result);
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(directionRepository);
    }

    /**
     * Проверяет, что если пользователь не зарегистрирован, сервис сначала регистрирует его.
     */
    @Test
    void Given_userNotRegistered_When_createEnrollment_Then_registersUserFirst() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyService spyService = spy(service);

        User user = User.builder()
                .id(61L)
                .tgId("tg-61")
                .neostudyExternalId(null)
                .build();

        User registered = User.builder()
                .id(61L)
                .tgId("tg-61")
                .neostudyExternalId("neo-user-61")
                .build();

        Direction direction = Direction.builder()
                .id(610L)
                .code("DIR-61")
                .name("Dir")
                .neostudyExternalId("neo-course-61")
                .build();

        // user not registered -> service must register first
        doReturn(registered).when(spyService).registerUser(eq(user));

        // mapper is @Mock => must return non-null request
        when(neoStudyMapper.toNeoStudyEnrollmentRequest(anyString(), anyString(), anyString()))
                .thenAnswer(inv -> NeoStudyEnrollmentRequest.builder()
                        .userId(inv.getArgument(0))
                        .courseId(inv.getArgument(1))
                        .status(inv.getArgument(2))
                        .enrollmentDate("2026-01-01T00:00:00Z")
                        .build());

        NeoStudyEnrollmentResponse response = NeoStudyEnrollmentResponse.builder()
                .id("enroll-61")
                .status("active")
                .progress(10)
                .build();

        when(neoStudyClient.createEnrollment(any())).thenReturn(Mono.just(response));

        // Act
        NeoStudyEnrollmentResponse result = spyService.createEnrollment(user, direction, "active");

        // Assert
        assertNotNull(result);
        assertEquals("enroll-61", result.getId());

        verify(spyService).registerUser(eq(user));

        ArgumentCaptor<NeoStudyEnrollmentRequest> reqCaptor = ArgumentCaptor.forClass(NeoStudyEnrollmentRequest.class);
        verify(neoStudyClient).createEnrollment(reqCaptor.capture());

        assertEquals("neo-user-61", reqCaptor.getValue().getUserId());
        assertEquals("neo-course-61", reqCaptor.getValue().getCourseId());
        assertEquals("active", reqCaptor.getValue().getStatus());
        assertNotNull(reqCaptor.getValue().getEnrollmentDate());
    }

    /**
     * Проверяет, что если курс не синхронизирован, сервис вызывает syncCourses() и перечитывает направление.
     */
    @Test
    void Given_directionNotSynced_When_createEnrollment_Then_syncsCoursesAndReloadsDirection() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyService spyService = spy(service);

        User user = User.builder()
                .id(62L)
                .tgId("tg-62")
                .neostudyExternalId("neo-user-62")
                .build();

        Direction direction = Direction.builder()
                .id(620L)
                .code("DIR-62")
                .name("Dir")
                .neostudyExternalId(null)
                .build();

        // after syncCourses direction must become synced (simulate reload from repository)
        Direction reloaded = Direction.builder()
                .id(620L)
                .code("DIR-62")
                .name("Dir")
                .neostudyExternalId("neo-course-62")
                .build();

        doReturn(List.of(reloaded)).when(spyService).syncCourses();
        when(directionRepository.findById(eq(620L))).thenReturn(Optional.of(reloaded));

        // mapper is @Mock => must return non-null request
        when(neoStudyMapper.toNeoStudyEnrollmentRequest(anyString(), anyString(), anyString()))
                .thenAnswer(inv -> NeoStudyEnrollmentRequest.builder()
                        .userId(inv.getArgument(0))
                        .courseId(inv.getArgument(1))
                        .status(inv.getArgument(2))
                        .enrollmentDate("2026-01-01T00:00:00Z")
                        .build());

        when(neoStudyClient.createEnrollment(any()))
                .thenReturn(Mono.just(NeoStudyEnrollmentResponse.builder()
                        .id("enroll-62")
                        .status("active")
                        .build()));

        // Act
        NeoStudyEnrollmentResponse result = spyService.createEnrollment(user, direction, "active");

        // Assert
        assertNotNull(result);
        assertEquals("enroll-62", result.getId());

        verify(spyService).syncCourses();
        verify(directionRepository).findById(eq(620L));

        ArgumentCaptor<NeoStudyEnrollmentRequest> reqCaptor = ArgumentCaptor.forClass(NeoStudyEnrollmentRequest.class);
        verify(neoStudyClient).createEnrollment(reqCaptor.capture());

        assertEquals("neo-user-62", reqCaptor.getValue().getUserId());
        assertEquals("neo-course-62", reqCaptor.getValue().getCourseId());
        assertEquals("active", reqCaptor.getValue().getStatus());
        assertNotNull(reqCaptor.getValue().getEnrollmentDate());
    }

    /**
     * Проверяет, что если направление не найдено после синхронизации, выбрасывается IllegalArgumentException.
     */
    @Test
    void Given_directionNotFoundAfterSync_When_createEnrollment_Then_throwsIllegalArgumentException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyService spyService = spy(service);

        User user = User.builder().id(63L).tgId("tg-63").neostudyExternalId("neo-user-63").build();
        Direction direction = Direction.builder().id(630L).code("DIR-63").name("Dir").neostudyExternalId(null).build();

        doReturn(List.of()).when(spyService).syncCourses();
        when(directionRepository.findById(eq(630L))).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> spyService.createEnrollment(user, direction, "active")
        );
        assertTrue(ex.getMessage().contains("Направление не найдено"));
        verifyNoInteractions(neoStudyClient);
    }

    /**
     * Проверяет, что при ошибке клиента создание записи оборачивается в NeoStudyException.
     */
    @Test
    void Given_clientThrows_When_createEnrollment_Then_wrapsIntoNeoStudyException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        User user = User.builder().id(64L).tgId("tg-64").neostudyExternalId("neo-user-64").build();
        Direction direction = Direction.builder().id(640L).code("DIR-64").name("Dir").neostudyExternalId("neo-course-64").build();

        when(neoStudyClient.createEnrollment(any(NeoStudyEnrollmentRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("boom")));

        // Act + Assert
        NeoStudyException ex = assertThrows(NeoStudyException.class, () -> service.createEnrollment(user, direction, "active"));
        assertTrue(ex.getMessage().contains("Не удалось создать запись в NeoStudy"));
    }

    // =========================================================================
    // syncEnrollmentStatus
    // =========================================================================

    /**
     * Проверяет пропуск синхронизации статуса записи при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_syncEnrollmentStatus_Then_returnsNull() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);

        // Act
        NeoStudyEnrollmentResponse result = service.syncEnrollmentStatus("enroll-1");

        // Assert
        assertNull(result);
        verifyNoInteractions(neoStudyClient);
    }

    /**
     * Проверяет успешную синхронизацию статуса записи через NeoStudyClient.
     */
    @Test
    void Given_clientReturnsEnrollment_When_syncEnrollmentStatus_Then_returnsResponse() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyEnrollmentResponse response = NeoStudyEnrollmentResponse.builder()
                .id("enroll-2")
                .status("active")
                .progress(33)
                .build();

        when(neoStudyClient.syncEnrollmentStatus(eq("enroll-2"))).thenReturn(Mono.just(response));

        // Act
        NeoStudyEnrollmentResponse result = service.syncEnrollmentStatus("enroll-2");

        // Assert
        assertNotNull(result);
        assertEquals("enroll-2", result.getId());
        assertEquals("active", result.getStatus());
        assertEquals(33, result.getProgress());
        verify(neoStudyClient).syncEnrollmentStatus(eq("enroll-2"));
    }

    /**
     * Проверяет оборачивание ошибки в NeoStudyException при синхронизации статуса записи.
     */
    @Test
    void Given_clientThrows_When_syncEnrollmentStatus_Then_wrapsIntoNeoStudyException() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);
        when(neoStudyClient.syncEnrollmentStatus(eq("enroll-3"))).thenReturn(Mono.error(new RuntimeException("boom")));

        // Act + Assert
        NeoStudyException ex = assertThrows(NeoStudyException.class, () -> service.syncEnrollmentStatus("enroll-3"));
        assertTrue(ex.getMessage().contains("Не удалось синхронизировать статус записи"));
    }

    // =========================================================================
    // processWebhook
    // =========================================================================

    /**
     * Проверяет игнорирование вебхука при выключенной интеграции.
     */
    @Test
    void Given_integrationDisabled_When_processWebhook_Then_doesNothing() {
        // Arrange
        when(properties.isEnabled()).thenReturn(false);

        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType("enrollment.updated")
                .timestamp(LocalDateTime.now())
                .data(Map.of("status", "active"))
                .build();

        // Act
        assertDoesNotThrow(() -> service.processWebhook(payload));

        // Assert
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(directionRepository);
    }

    /**
     * Проверяет, что вебхук без eventType не приводит к ошибке.
     */
    @Test
    void Given_nullEventType_When_processWebhook_Then_doesNothing() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType(null)
                .timestamp(LocalDateTime.now())
                .data(Map.of("any", "value"))
                .build();

        // Act + Assert
        assertDoesNotThrow(() -> service.processWebhook(payload));
        verifyNoInteractions(neoStudyClient);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(directionRepository);
    }

    /**
     * Проверяет обработку вебхука enrollment.updated (заглушка) без исключений.
     */
    @Test
    void Given_enrollmentUpdatedEvent_When_processWebhook_Then_doesNotThrow() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType("enrollment.updated")
                .timestamp(LocalDateTime.now())
                .data(Map.of("status", "active"))
                .build();

        // Act + Assert
        assertDoesNotThrow(() -> service.processWebhook(payload));
    }

    /**
     * Проверяет обработку вебхука user.updated (заглушка) без исключений.
     */
    @Test
    void Given_userUpdatedEvent_When_processWebhook_Then_doesNotThrow() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType("user.updated")
                .timestamp(LocalDateTime.now())
                .data(Map.of("id", "neo-1"))
                .build();

        // Act + Assert
        assertDoesNotThrow(() -> service.processWebhook(payload));
    }

    /**
     * Проверяет обработку неизвестного eventType без исключений.
     */
    @Test
    void Given_unknownEventType_When_processWebhook_Then_doesNotThrow() {
        // Arrange
        when(properties.isEnabled()).thenReturn(true);

        NeoStudyWebhookPayload payload = NeoStudyWebhookPayload.builder()
                .eventType("unknown.event")
                .timestamp(LocalDateTime.now())
                .data(Map.of("k", "v"))
                .build();

        // Act + Assert
        assertDoesNotThrow(() -> service.processWebhook(payload));
    }
}
