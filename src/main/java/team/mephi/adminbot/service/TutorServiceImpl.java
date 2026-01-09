package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
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
        Tutor user = dto.getId() != null
                ? tutorRepository.findById(dto.getId()).orElse(new Tutor())
                : new Tutor();
        user.setUserName(dto.getLastName() + " " + dto.getFirstName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTgId());
        var prevActive = user.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(st -> st.getStudent().getId()).toList();
        var current = dto.getStudents().stream().map(SimpleUser::getId).collect(Collectors.toSet());
        var td = dto.getStudents().stream().filter(s -> !prevActive.contains(s.getId())).map(
                u -> StudentTutor.builder()
                        .mode(userRepository.countByIdWithTutorAssignment(u.getId()) > 0 ? StudentTutorMode.REASSIGN : StudentTutorMode.INITIAL)
                        .tutor(Tutor.builder().id(dto.getId()).build())
                        .student(User.builder().id(u.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
        user.getStudentAssignments().forEach(s -> {if (!current.contains(s.getStudent().getId())) s.setIsActive(false);});
        user.getStudentAssignments().addAll(td);
        user = tutorRepository.save(user);
        return mapToSimpleUser(user);
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
