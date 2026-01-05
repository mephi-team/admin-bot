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
        var td = dto.getStudents().stream().map(
                u -> StudentTutor.builder()
                        .mode(StudentTutorMode.INITIAL)
                        .tutor(Tutor.builder().id(dto.getId()).build())
                        .student(User.builder().id(u.getId()).build())
                        .build()
        ).collect(Collectors.toSet());
        user.getStudentAssignments().clear();
        user.getStudentAssignments().addAll(td);
        user = tutorRepository.save(user);
        return SimpleUser.builder().build();
    }
}
