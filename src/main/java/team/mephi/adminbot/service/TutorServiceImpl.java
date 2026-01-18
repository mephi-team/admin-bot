package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.TutorDto;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.StudentTutorMode;
import team.mephi.adminbot.repository.StudentTutorRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для управления репетиторами.
 */
@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final StudentTutorRepository studentTutorRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param tutorRepository        репозиторий для управления репетиторами.
     * @param studentTutorRepository репозиторий для управления назначениями студентов репетиторам.
     * @param userRepository         репозиторий для управления пользователями.
     */
    public TutorServiceImpl(TutorRepository tutorRepository, StudentTutorRepository studentTutorRepository, UserRepository userRepository) {
        this.tutorRepository = tutorRepository;
        this.studentTutorRepository = studentTutorRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SimpleTutor save(SimpleTutor dto) {
        Tutor tutor = dto.getId() != null
                ? tutorRepository.findById(dto.getId()).orElse(new Tutor())
                : new Tutor();
        tutor.setUserName(dto.getLastName() + " " + dto.getFirstName());
        tutor.setFirstName(dto.getFirstName());
        tutor.setLastName(dto.getLastName());
        tutor.setEmail(dto.getEmail());
        tutor.setTgId(dto.getTgId());
        var prevDirection = tutor.getDirections().stream().map(Direction::getId).toList();
        var currentDirection = dto.getDirections().stream().map(SimpleDirection::getId).collect(Collectors.toSet());
        var td = dto.getDirections().stream().filter(s -> !prevDirection.contains(s.getId())).map(
                t -> Direction.builder().id(t.getId()).build()
        ).collect(Collectors.toSet());
        tutor.getDirections().retainAll(tutor.getDirections().stream().filter(s -> currentDirection.contains(s.getId())).toList());
        tutor.getDirections().addAll(td);

        var prevActiveAssignment = tutor.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(st -> st.getStudent().getId()).toList();
        var currentAssignment = dto.getStudents().stream().map(SimpleUser::getId).collect(Collectors.toSet());
        tutor.getStudentAssignments().forEach(s -> {
            if (!currentAssignment.contains(s.getStudent().getId())) s.setIsActive(false);
        });

        if (Objects.nonNull(dto.getId())) {
            var st = prepareStudentTutor(dto.getStudents().stream().filter(s -> !prevActiveAssignment.contains(s.getId())).toList(), dto.getId());
            tutor.getStudentAssignments().addAll(st);
            Tutor updatedTutor = tutorRepository.save(tutor);
            return mapToSimpleUser(updatedTutor);
        } else {
            Tutor updatedTutor = tutorRepository.save(tutor);
            var st = prepareStudentTutor(dto.getStudents().stream().filter(s -> !prevActiveAssignment.contains(s.getId())).toList(), updatedTutor.getId());
            studentTutorRepository.saveAll(st);
            return mapToSimpleUser(updatedTutor);
        }
    }

    /**
     * Подготовка назначений студентов репетитору.
     *
     * @param students список студентов.
     * @param tutorId  идентификатор репетитора.
     * @return набор назначений студентов репетитору.
     */
    private Set<StudentTutor> prepareStudentTutor(List<SimpleUser> students, Long tutorId) {
        return students.stream().map(
                u -> StudentTutor.builder()
                        .mode(userRepository.countByIdWithTutorAssignment(u.getId()) > 0 ? StudentTutorMode.REASSIGN : StudentTutorMode.INITIAL)
                        .tutor(Tutor.builder().id(tutorId).build())
                        .student(User.builder().id(u.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
    }

    @Override
    public Optional<SimpleTutor> findById(Long id) {
        return tutorRepository.findByIdWithStudent(id).map(this::mapToSimpleUser);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        tutorRepository.deleteAllById(ids);
    }

    @Override
    public void blockAllById(Iterable<Long> ids) {
        tutorRepository.blockAllById(ids);
    }

    @Override
    public Stream<TutorDto> findAllByName(String name, Pageable pageable) {
        return tutorRepository.findAllByName(name, pageable)
                .stream()
                .map(this::mapToTutorDto);
    }

    @Override
    public Stream<SimpleTutor> findAllWithDirectionsAndStudents(String name, Pageable pageable) {
        return tutorRepository.findAllWithDirectionsAndStudents(name, pageable)
                .stream()
                .map(this::mapToSimpleUser);
    }

    @Override
    public Integer countByName(String name) {
        return tutorRepository.countByName(name);
    }

    /**
     * Преобразует объект Tutor в SimpleTutor.
     *
     * @param tutor объект Tutor для преобразования.
     * @return преобразованный объект SimpleTutor.
     */
    private SimpleTutor mapToSimpleUser(Tutor tutor) {
        return SimpleTutor.builder()
                .id(tutor.getId())
                .role("TUTOR")
                .fullName(tutor.getLastName() + " " + tutor.getFirstName())
                .firstName(tutor.getFirstName())
                .lastName(tutor.getLastName())
                .email(tutor.getEmail())
                .phoneNumber(tutor.getPhone())
                .tgId(tutor.getTgId())
                .studentCount(tutor.getStudentAssignments().stream().filter(StudentTutor::getIsActive).toList().size())
                .students(tutor.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(s -> SimpleUser.builder().id(s.getStudent().getId()).fullName(s.getStudent().getUserName()).tgId(s.getStudent().getTgId()).build()).toList())
                .directions(tutor.getDirections().stream().map(d -> SimpleDirection.builder().id(d.getId()).name(d.getName()).build()).collect(Collectors.toList()))
                .status("ACTIVE")
                .build();
    }

    /**
     * Преобразует объект Tutor в TutorDto.
     *
     * @param tutor объект Tutor для преобразования.
     * @return преобразованный объект TutorDto.
     */
    private TutorDto mapToTutorDto(Tutor tutor) {
        return TutorDto.builder()
                .id(tutor.getId())
                .fullName(tutor.getLastName() + " " + tutor.getFirstName())
                .build();
    }
}
