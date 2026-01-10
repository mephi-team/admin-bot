package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimplePd;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.StudentTutorMode;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.ExpertRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ExpertServiceImpl implements ExpertService {

    private final ExpertRepository expertRepository;

    public ExpertServiceImpl(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    @Override
    @Transactional
    public SimpleUser save(SimpleUser dto) {
        Expert expert = dto.getId() != null
                ? expertRepository.findById(dto.getId()).orElse(new Expert())
                : new Expert();

        expert.setRole(Role.builder().code(dto.getRole()).build());
        expert.setUserName(dto.getFirstName() + " " + dto.getLastName());
        expert.setFirstName(dto.getFirstName());
        expert.setLastName(dto.getLastName());
        expert.setEmail(dto.getEmail());
        expert.setTgId(dto.getTgId());
        expert.setPhoneNumber(dto.getPhoneNumber());
        expert.setCity(dto.getCity());
        expert.setDirection(Direction.builder().id(dto.getDirection().getId()).name(dto.getDirection().getName()).build());
        expert.setCohort(dto.getCohort());
        var currentAssignment = expert.getTutorAssignments();
        if (Objects.nonNull(dto.getTutor()) && Objects.nonNull(dto.getTutor().getId()) && currentAssignment.stream().noneMatch(a -> a.getIsActive() && a.getTutor().getId().equals(dto.getTutor().getId()))) {
            expert.getTutorAssignments().forEach(ta -> ta.setIsActive(false));
            expert.getTutorAssignments().add(StudentTutor.builder()
                    .student(expert)
                    .mode(currentAssignment.isEmpty() ? StudentTutorMode.INITIAL : StudentTutorMode.REASSIGN)
                    .isActive(true)
                    .tutor(Tutor.builder().id(dto.getTutor().getId()).build())
                    .assignedAt(Instant.now())
                    .build());
        }

        if (Objects.isNull(expert.getStatus())){
            expert.setStatus(UserStatus.ACTIVE);
        }

        expert = expertRepository.save(expert);

        return mapToSimple(expert);
    }

    @Override
    public Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable) {
        return expertRepository.findAllByRoleAndName(role, query, pageable)
                .stream()
                .map(this::mapToSimple);
    }

    @Override
    public Integer countByRoleAndName(String role, String query) {
        return expertRepository.countByRoleAndName(role, query);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        expertRepository.deleteAllById(ids);
    }

    private SimpleUser mapToSimple(User user) {
        var tutor = user.getTutorAssignments().stream().filter(StudentTutor::getIsActive).findAny().orElseGet(() -> StudentTutor.builder().tutor(Tutor.builder().userName("").firstName("").lastName("").build()).build()).getTutor();
        return SimpleUser.builder()
                .id(user.getId())
                .role(user.getRole().getCode())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .tgId(user.getTgId())
                .tgName(user.getTgName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pdConsent(user.getPdConsent())
                .pdConsentLog(user.getPdConsentLogs().stream().map(pd -> SimplePd.builder().id(pd.getId()).source(pd.getSource()).status(pd.getStatus().name()).build()).toList())
                .status(user.getStatus().name())
                .city(user.getCity())
                .direction(Objects.nonNull(user.getDirection()) ? SimpleDirection.builder().id(user.getDirection().getId()).name(user.getDirection().getName()).build() : null)
                .cohort(user.getCohort())
                .tutor(Objects.isNull(tutor) ? SimpleTutor.builder().build() : SimpleTutor.builder().id(tutor.getId()).fullName(tutor.getLastName() + " " + tutor.getFirstName()).build())
                .build();
    }
}
