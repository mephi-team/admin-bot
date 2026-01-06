package team.mephi.adminbot.vaadin.users.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.StudentTutor;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.StudentTutorMode;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorService;

import java.util.stream.Collectors;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;

    public TutorServiceImpl(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Override
    @Transactional
    public SimpleUser save(SimpleUser dto) {
        Tutor user = dto.getId() != null
                ? tutorRepository.findById(dto.getId()).orElse(new Tutor())
                : new Tutor();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTgId());
        var prevActive = user.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(st -> st.getStudent().getId()).toList();
        var prevInActive = user.getStudentAssignments().stream().filter(u -> !u.getIsActive()).map(st -> st.getStudent().getId()).toList();
        var current = dto.getStudents().stream().map(SimpleUser::getId).collect(Collectors.toSet());
        current.retainAll(prevActive);
        var td = dto.getStudents().stream().filter(s -> !prevActive.contains(s.getId())).map(
                u -> StudentTutor.builder()
                        .mode(prevInActive.contains(u.getId()) ? StudentTutorMode.REASSIGN : StudentTutorMode.INITIAL)
                        .tutor(Tutor.builder().id(dto.getId()).build())
                        .student(User.builder().id(u.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
        user.getStudentAssignments().forEach(s -> {if (!current.contains(s.getStudent().getId())) s.setIsActive(false);});
        user.getStudentAssignments().addAll(td);
        user = tutorRepository.save(user);
        return SimpleUser.builder()
                .id(user.getId())
                .role("tutor")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .tgId(user.getTgId())
                .studentCount(user.getStudentAssignments().stream().filter(StudentTutor::getIsActive).toList().size())
                .students(user.getStudentAssignments().stream().filter(StudentTutor::getIsActive).map(s -> SimpleUser.builder().id(s.getStudent().getId()).fullName(s.getStudent().getUserName()).tgId(s.getStudent().getTgId()).build()).toList())
                .build();
    }
}
