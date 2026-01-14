package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.PdConsentLog;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.ConsentStatus;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для UserServiceImpl.
 * Покрывают: выборки пользователей, сохранение и назначения тьюторов.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TutorRepository tutorRepository;

    /**
     * Проверяет маппинг списка пользователей в DTO.
     */
    @Test
    void Given_users_When_getAllUsers_Then_mapsToDto() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .userName("Ivan Ivanov")
                .tgId("tg1")
                .build();
        when(userRepository.findAll()).thenReturn(List.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<UserDto> result = service.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Ivan Ivanov", result.getFirst().getUserName());
        assertEquals("tg1", result.getFirst().getTgName());
    }

    /**
     * Проверяет пагинацию списка пользователей.
     */
    @Test
    void Given_pageable_When_getAllUsersPaged_Then_appliesOffsetAndLimit() {
        // Arrange
        List<User> users = List.of(
                User.builder().id(1L).userName("First").build(),
                User.builder().id(2L).userName("Second").build()
        );
        when(userRepository.searchAll(eq("q"))).thenReturn(users);
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<UserDto> result = service.getAllUsers(PageRequest.of(1, 1), "q");

        // Assert
        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().getId());
    }

    /**
     * Проверяет поиск пользователя по идентификатору.
     */
    @Test
    void Given_id_When_getById_Then_returnsDto() {
        // Arrange
        User user = User.builder().id(5L).userName("User").build();
        when(userRepository.findById(eq(5L))).thenReturn(Optional.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Optional<UserDto> result = service.getById(5L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("User", result.get().getUserName());
    }

    /**
     * Проверяет получение агрегированных счетчиков.
     */
    @Test
    void Given_repositoryCounts_When_getAllCounts_Then_returnsMap() {
        // Arrange
        when(userRepository.countsByRole()).thenReturn(Map.of("STUDENT", 2L));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Map<String, Long> result = service.getAllCounts();

        // Assert
        assertEquals(2L, result.get("STUDENT"));
    }

    /**
     * Проверяет маппинг пользователя в простой DTO.
     */
    @Test
    void Given_userWithTutor_When_findById_Then_mapsToSimple() {
        // Arrange
        Tutor tutor = Tutor.builder().id(9L).firstName("Petr").lastName("Petrov").build();
        StudentTutor assignment = StudentTutor.builder()
                .id(3L)
                .tutor(tutor)
                .isActive(true)
                .build();
        User user = User.builder()
                .id(10L)
                .userName("Ivan Ivanov")
                .firstName("Ivan")
                .lastName("Ivanov")
                .role(Role.builder().code("STUDENT").build())
                .status(UserStatus.ACTIVE)
                .direction(Direction.builder().id(4L).name("Math").build())
                .pdConsentLogs(List.of(PdConsentLog.builder().id(1L).source("FORM").status(ConsentStatus.GRANTED).build()))
                .tutorAssignments(new HashSet<>(Set.of(assignment)))
                .build();
        when(userRepository.findByIdWithRoleAndDirection(eq(10L))).thenReturn(Optional.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Optional<SimpleUser> result = service.findById(10L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("STUDENT", result.get().getRole());
        assertEquals("Ivan Ivanov", result.get().getFullName());
        assertEquals("Petrov Petr", result.get().getTutor().getFullName());
    }

    /**
     * Проверяет сохранение нового пользователя с дефолтным статусом.
     */
    @Test
    void Given_newUserDto_When_save_Then_setsDefaults() {
        // Arrange
        SimpleUser dto = SimpleUser.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .role("STUDENT")
                .direction(new HashSet<>(Set.of(SimpleDirection.builder().id(1L).name("Math").build())))
                .build();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0, User.class));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        SimpleUser result = service.save(dto);

        // Assert
        assertEquals("Ivan Ivanov", result.getFullName());
        assertEquals("ACTIVE", result.getStatus());
    }

    /**
     * Проверяет добавление назначения тьютору при сохранении.
     */
    @Test
    void Given_existingUserAndTutor_When_save_Then_reassignsTutor() {
        // Arrange
        Tutor existingTutor = Tutor.builder().id(1L).build();
        StudentTutor existingAssignment = StudentTutor.builder()
                .id(1L)
                .tutor(existingTutor)
                .isActive(true)
                .build();
        User storedUser = User.builder()
                .id(7L)
                .firstName("Anna")
                .lastName("Ivanova")
                .role(Role.builder().code("STUDENT").build())
                .status(UserStatus.ACTIVE)
                .tutorAssignments(new HashSet<>(Set.of(existingAssignment)))
                .build();
        when(userRepository.findById(eq(7L))).thenReturn(Optional.of(storedUser));
        when(userRepository.save(eq(storedUser))).thenReturn(storedUser);
        SimpleUser dto = SimpleUser.builder()
                .id(7L)
                .firstName("Anna")
                .lastName("Ivanova")
                .role("STUDENT")
                .direction(new HashSet<>(Set.of(SimpleDirection.builder().id(1L).name("Math").build())))
                .tutor(SimpleTutor.builder().id(2L).build())
                .build();
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        SimpleUser result = service.save(dto);

        // Assert
        assertEquals("Anna Ivanova", result.getFullName());
        assertEquals(2, storedUser.getTutorAssignments().size());
        assertEquals(1, storedUser.getTutorAssignments().stream().filter(StudentTutor::getIsActive).count());
    }

    /**
     * Проверяет удаление пользователей по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(userRepository).deleteAllById(eq(ids));
    }

    /**
     * Проверяет блокировку пользователей по идентификаторам.
     */
    @Test
    void Given_ids_When_blockAllById_Then_callsRepository() {
        // Arrange
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);
        List<Long> ids = List.of(3L, 4L);

        // Act
        service.blockAllById(ids);

        // Assert
        verify(userRepository).blockAllById(eq(ids));
    }

    /**
     * Проверяет выборку пользователей по роли и имени.
     */
    @Test
    void Given_roleAndName_When_findAllByRoleAndName_Then_mapsStream() {
        // Arrange
        User user = User.builder()
                .id(8L)
                .userName("Ivan Ivanov")
                .firstName("Ivan")
                .lastName("Ivanov")
                .role(Role.builder().code("STUDENT").build())
                .status(UserStatus.ACTIVE)
                .build();
        when(userRepository.findAllByRoleAndName(eq("STUDENT"), eq("Iv"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<SimpleUser> result = service.findAllByRoleAndName("STUDENT", "Iv", PageRequest.of(0, 1))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Ivan Ivanov", result.getFirst().getFullName());
    }

    /**
     * Проверяет выборку пользователей для фильтров.
     */
    @Test
    void Given_filters_When_findAllByRoleCodeLike_Then_mapsLightweightDto() {
        // Arrange
        User user = User.builder().id(12L).userName("User One").tgId("tg1").build();
        when(userRepository.findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(
                eq("STUDENT"), eq("2024"), eq(1L), eq("Moscow"), eq(2L)))
                .thenReturn(List.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<SimpleUser> result = service.findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(
                        "STUDENT", "2024", 1L, "Moscow", 2L, PageRequest.of(0, 10))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("User One", result.getFirst().getFullName());
        assertEquals("tg1", result.getFirst().getTgId());
    }

    /**
     * Проверяет подсчёт пользователей по роли и имени.
     */
    @Test
    void Given_query_When_countByRoleAndName_Then_returnsCount() {
        // Arrange
        when(userRepository.countByRoleAndName(eq("STUDENT"), eq("Iv"))).thenReturn(6);
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Integer result = service.countByRoleAndName("STUDENT", "Iv");

        // Assert
        assertEquals(6, result);
    }

    /**
     * Проверяет инициализацию списка кураторов.
     */
    @Test
    void Given_tutors_When_findAllCurators_Then_initializesList() {
        // Arrange
        Tutor tutor = Tutor.builder().id(2L).userName("Tutor One").build();
        when(tutorRepository.findAll()).thenReturn(List.of(tutor));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<UserDto> result = service.findAllCurators(PageRequest.of(0, 5), "");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tutor One", result.get(1).getUserName());
    }

    /**
     * Проверяет поиск куратора по имени.
     */
    @Test
    void Given_name_When_findCuratorByUserName_Then_returnsMatch() {
        // Arrange
        Tutor tutor = Tutor.builder().id(3L).userName("Curator").build();
        when(tutorRepository.findAll()).thenReturn(List.of(tutor));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Optional<UserDto> result = service.findCuratorByUserName("Curator");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getId());
    }

    /**
     * Проверяет возврат заглушки при пустом списке для кураторства.
     */
    @Test
    void Given_emptyAssignments_When_findAllForCuratorship_Then_returnsPlaceholder() {
        // Arrange
        when(userRepository.findAllStudentsWithTutorAssignments(eq("name"), eq("STUDENT"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of());
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<SimpleUser> result = service.findAllForCuratorship("name", PageRequest.of(0, 1))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Нет элементов для выбора", result.getFirst().getFullName());
    }

    /**
     * Проверяет маппинг списка для кураторства.
     */
    @Test
    void Given_assignments_When_findAllForCuratorship_Then_mapsUsers() {
        // Arrange
        User user = User.builder().id(5L).userName("User A").firstName("User").lastName("A").tgId("tg").build();
        when(userRepository.findAllStudentsWithTutorAssignments(eq("name"), eq("STUDENT"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(user));
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        List<SimpleUser> result = service.findAllForCuratorship("name", PageRequest.of(0, 1))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("User A", result.getFirst().getFullName());
    }

    /**
     * Проверяет возврат счётчика для кураторства.
     */
    @Test
    void Given_counts_When_countAllForCuratorship_Then_usesFallback() {
        // Arrange
        when(userRepository.countAllStudentsWithTutorAssignments(eq("name"), eq("STUDENT"))).thenReturn(0);
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Integer result = service.countAllForCuratorship("name");

        // Assert
        assertEquals(1, result);
    }

    /**
     * Проверяет возврат фактического счётчика кураторства.
     */
    @Test
    void Given_positiveCount_When_countAllForCuratorship_Then_returnsCount() {
        // Arrange
        when(userRepository.countAllStudentsWithTutorAssignments(eq("name"), eq("STUDENT"))).thenReturn(5);
        UserServiceImpl service = new UserServiceImpl(userRepository, tutorRepository);

        // Act
        Integer result = service.countAllForCuratorship("name");

        // Assert
        assertEquals(5, result);
    }
}
