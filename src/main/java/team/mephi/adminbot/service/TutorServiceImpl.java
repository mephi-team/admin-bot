package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.StudentTutorMode;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;

    public TutorServiceImpl(TutorRepository tutorRepository, UserRepository userRepository) {
        this.tutorRepository = tutorRepository;
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
        var prevActiveAssignment = tutor.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(st -> st.getStudent().getId()).toList();
        var currentAssignment = dto.getStudents().stream().map(SimpleUser::getId).collect(Collectors.toSet());
        var sa = dto.getStudents().stream().filter(s -> !prevActiveAssignment.contains(s.getId())).map(
                u -> StudentTutor.builder()
                        .mode(userRepository.countByIdWithTutorAssignment(u.getId()) > 0 ? StudentTutorMode.REASSIGN : StudentTutorMode.INITIAL)
                        .tutor(Tutor.builder().id(dto.getId()).build())
                        .student(User.builder().id(u.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
        tutor.getStudentAssignments().forEach(s -> {if (!currentAssignment.contains(s.getStudent().getId())) s.setIsActive(false);});
        tutor.getStudentAssignments().addAll(sa);
        var prevDirection = tutor.getDirections().stream().map(TutorDirection::getDirectionId).toList();
        var currentDirection = dto.getDirections().stream().map(SimpleDirection::getId).collect(Collectors.toSet());
        var td = dto.getDirections().stream().filter(s -> !prevDirection.contains(s.getId())).map(
                t -> TutorDirection.builder()
                        .tutorId(dto.getId())
                        .directionId(t.getId())
                        .direction(Direction.builder().id(t.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
        tutor.getDirections().retainAll(tutor.getDirections().stream().filter(s -> currentDirection.contains(s.getDirectionId())).toList());
        tutor.getDirections().addAll(td);
        tutor = tutorRepository.save(tutor);
        return mapToSimpleUser(tutor);
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
    public Stream<SimpleTutor> findAllByName(String name, Pageable pageable) {
        return tutorRepository.findAllWithDirectionsAndStudents(name, pageable)
                .stream()
                .map(this::mapToSimpleUser);
    }

    @Override
    public Integer countByName(String name) {
        return tutorRepository.countByName(name);
    }

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
                .directions(tutor.getDirections().stream().map(d -> SimpleDirection.builder().id(d.getDirection().getId()).name(d.getDirection().getName()).build()).collect(Collectors.toList()))
                .status("ACTIVE")
                .build();
    }
}
