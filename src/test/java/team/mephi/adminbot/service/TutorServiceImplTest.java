package team.mephi.adminbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.StudentTutorRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link TutorServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class TutorServiceImplTest {
    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private StudentTutorRepository studentTutorRepository;

    @Mock
    private UserRepository userRepository;

    private TutorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TutorServiceImpl(tutorRepository, studentTutorRepository, userRepository);
    }

    /**
     * Проверяет маппинг данных тьютора при поиске по идентификатору.
     */
    @Test
    void givenTutor_WhenFindByIdCalled_ThenMapsToSimpleTutor() {
        // Arrange
        Tutor tutor = Tutor.builder()
                .id(11L)
                .firstName("Ivan")
                .lastName("Petrov")
                .email("ivan@example.com")
                .tgId("tg")
                .directions(Set.of(Direction.builder().id(3L).name("QA").build()))
                .build();
        StudentTutor assignment = StudentTutor.builder()
                .isActive(true)
                .student(User.builder().id(55L).userName("Student Name").tgId("student_tg").build())
                .tutor(tutor)
                .build();
        tutor.getStudentAssignments().add(assignment);

        when(tutorRepository.findByIdWithStudent(11L)).thenReturn(Optional.of(tutor));

        // Act
        Optional<SimpleTutor> result = service.findById(11L);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .extracting(SimpleTutor::getId, SimpleTutor::getFullName, SimpleTutor::getStudentCount)
                .containsExactly(11L, "Petrov Ivan", 1);
    }
}
