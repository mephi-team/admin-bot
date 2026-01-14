package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Expert;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.ExpertRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для ExpertServiceImpl.
 * Покрывают: сохранение экспертов и выборки по имени.
 */
@ExtendWith(MockitoExtension.class)
class ExpertServiceImplTest {
    @Mock
    private ExpertRepository expertRepository;

    /**
     * Проверяет обновление направлений и статуса эксперта.
     */
    @Test
    void Given_existingExpert_When_save_Then_updatesDirections() {
        // Arrange
        Expert expert = Expert.builder()
                .id(2L)
                .role(Role.builder().code("EXPERT").build())
                .directions(Set.of(
                        Direction.builder().id(1L).name("Math").build(),
                        Direction.builder().id(2L).name("Physics").build()
                ))
                .build();
        when(expertRepository.findById(eq(2L))).thenReturn(Optional.of(expert));
        when(expertRepository.save(any(Expert.class))).thenAnswer(invocation -> invocation.getArgument(0, Expert.class));
        SimpleUser dto = SimpleUser.builder()
                .id(2L)
                .firstName("Eva")
                .lastName("Stone")
                .role("EXPERT")
                .direction(Set.of(
                        SimpleDirection.builder().id(2L).name("Physics").build(),
                        SimpleDirection.builder().id(3L).name("Chemistry").build()
                ))
                .build();
        ExpertServiceImpl service = new ExpertServiceImpl(expertRepository);

        // Act
        SimpleUser result = service.save(dto);

        // Assert
        assertEquals("Eva Stone", result.getFullName());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(2, expert.getDirections().size());
    }

    /**
     * Проверяет выборку экспертов по роли и имени.
     */
    @Test
    void Given_roleAndName_When_findAllByRoleAndName_Then_mapsStream() {
        // Arrange
        Expert expert = Expert.builder()
                .id(5L)
                .userName("Expert One")
                .firstName("Expert")
                .lastName("One")
                .role(Role.builder().code("EXPERT").build())
                .status(UserStatus.ACTIVE)
                .directions(Set.of(Direction.builder().id(1L).name("Math").build()))
                .build();
        when(expertRepository.findAllByRoleAndName(eq("EXPERT"), eq("Ex"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(expert));
        ExpertServiceImpl service = new ExpertServiceImpl(expertRepository);

        // Act
        List<SimpleUser> result = service.findAllByRoleAndName("EXPERT", "Ex", PageRequest.of(0, 1))
                .collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("Expert One", result.getFirst().getFullName());
    }

    /**
     * Проверяет подсчёт экспертов по роли и имени.
     */
    @Test
    void Given_query_When_countByRoleAndName_Then_returnsCount() {
        // Arrange
        when(expertRepository.countByRoleAndName(eq("EXPERT"), eq("Ex"))).thenReturn(4);
        ExpertServiceImpl service = new ExpertServiceImpl(expertRepository);

        // Act
        Integer result = service.countByRoleAndName("EXPERT", "Ex");

        // Assert
        assertEquals(4, result);
    }

    /**
     * Проверяет удаление экспертов по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        ExpertServiceImpl service = new ExpertServiceImpl(expertRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(expertRepository).deleteAllById(eq(ids));
    }
}
