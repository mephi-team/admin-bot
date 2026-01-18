package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.repository.StudentTutorRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для TutorServiceImpl.
 * Покрывают: сохранение тьюторов и назначения студентов.
 */
@ExtendWith(MockitoExtension.class)
class TutorServiceImplTest {
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private StudentTutorRepository studentTutorRepository;
    @Mock
    private UserRepository userRepository;

    /**
     * Проверяет сохранение нового тьютора и назначений.
     */
    @Test
    void Given_newTutor_When_save_Then_createsAssignments() {
        // Arrange
        Tutor savedTutor = Tutor.builder().id(10L).firstName("Anna").lastName("Fox").build();
        when(tutorRepository.save(any(Tutor.class))).thenReturn(savedTutor);
        when(userRepository.countByIdWithTutorAssignment(eq(5L))).thenReturn(0);
        SimpleTutor dto = SimpleTutor.builder()
                .firstName("Anna")
                .lastName("Fox")
                .directions(List.of(SimpleDirection.builder().id(2L).build()))
                .students(List.of(SimpleUser.builder().id(5L).build()))
                .build();
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);

        // Act
        SimpleTutor result = service.save(dto);

        // Assert
        assertEquals("Fox Anna", result.getFullName());
        ArgumentCaptor<Set<StudentTutor>> captor = ArgumentCaptor.forClass(Set.class);
        verify(studentTutorRepository).saveAll(captor.capture());
        assertEquals(1, captor.getValue().size());
    }

    /**
     * Проверяет обновление назначений при существующем тьюторе.
     */
    @Test
    void Given_existingTutor_When_save_Then_updatesAssignmentsOnTutor() {
        // Arrange
        Tutor tutor = Tutor.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .directions(new HashSet<>(Set.of(Direction.builder().id(1L).build())))
                .studentAssignments(new HashSet<>(Set.of()))
                .build();
        when(tutorRepository.findById(eq(2L))).thenReturn(Optional.of(tutor));
        when(tutorRepository.save(eq(tutor))).thenReturn(tutor);
        when(userRepository.countByIdWithTutorAssignment(eq(7L))).thenReturn(1);
        SimpleTutor dto = SimpleTutor.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .directions(List.of(SimpleDirection.builder().id(1L).build()))
                .students(List.of(SimpleUser.builder().id(7L).build()))
                .build();
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);

        // Act
        SimpleTutor result = service.save(dto);

        // Assert
        assertEquals(1, tutor.getStudentAssignments().size());
        assertEquals("Doe John", result.getFullName());
    }

    /**
     * Проверяет поиск тьютора по идентификатору.
     */
    @Test
    void Given_id_When_findById_Then_mapsTutor() {
        // Arrange
        Tutor tutor = Tutor.builder().id(4L).firstName("Olga").lastName("Nova").build();
        when(tutorRepository.findByIdWithStudent(eq(4L))).thenReturn(Optional.of(tutor));
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);

        // Act
        Optional<SimpleTutor> result = service.findById(4L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Nova Olga", result.get().getFullName());
    }

    /**
     * Проверяет удаление тьюторов по идентификаторам.
     */
    @Test
    void Given_ids_When_deleteAllById_Then_callsRepository() {
        // Arrange
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);
        List<Long> ids = List.of(1L, 2L);

        // Act
        service.deleteAllById(ids);

        // Assert
        verify(tutorRepository).deleteAllById(eq(ids));
    }

    /**
     * Проверяет блокировку тьюторов по идентификаторам.
     */
    @Test
    void Given_ids_When_blockAllById_Then_callsRepository() {
        // Arrange
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);
        List<Long> ids = List.of(3L, 4L);

        // Act
        service.blockAllById(ids);

        // Assert
        verify(tutorRepository).blockAllById(eq(ids));
    }

    /**
     * Проверяет выборку тьюторов по имени.
     */
    @Test
    void Given_name_When_findAllByName_Then_mapsStream() {
        // Arrange
        Tutor tutor = Tutor.builder().id(6L).firstName("Alex").lastName("Ray").build();
        when(tutorRepository.findAllWithDirectionsAndStudents(eq("Al"), eq(PageRequest.of(0, 1))))
                .thenReturn(List.of(tutor));
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);

        // Act
        List<SimpleTutor> result = service.findAllWithDirectionsAndStudents("Al", PageRequest.of(0, 1)).toList();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Ray Alex", result.getFirst().getFullName());
    }

    /**
     * Проверяет подсчёт тьюторов по имени.
     */
    @Test
    void Given_name_When_countByName_Then_returnsCount() {
        // Arrange
        when(tutorRepository.countByName(eq("Al"))).thenReturn(3);
        TutorServiceImpl service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);

        // Act
        Integer result = service.countByName("Al");

        // Assert
        assertEquals(3, result);
    }
}
