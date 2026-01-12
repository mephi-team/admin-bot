package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimplePd;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.ExpertRepository;

import java.util.Objects;
import java.util.stream.Collectors;
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
        var prevActiveDirections = expert.getDirections().stream().map(Direction::getId).toList();
        var currentDirections = dto.getDirection().stream().map(SimpleDirection::getId).collect(Collectors.toSet());
        var directions = dto.getDirection().stream().filter(s -> !prevActiveDirections.contains(s.getId())).map(
                d -> Direction.builder().id(d.getId()).name(d.getName()).build()
        ).collect(Collectors.toSet());
        expert.getDirections().retainAll(expert.getDirections().stream().filter(s -> currentDirections.contains(s.getId())).toList());
        expert.getDirections().addAll(directions);
        expert.setCohort(dto.getCohort());
        if (Objects.isNull(expert.getStatus())) {
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

    private SimpleUser mapToSimple(Expert user) {
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
                .direction(Objects.nonNull(user.getDirections()) ? user.getDirections().stream().map(d -> SimpleDirection.builder().id(d.getId()).name(d.getName()).build()).collect(Collectors.toSet()) : null)
                .cohort(user.getCohort())
                .tutor(Objects.isNull(tutor) ? SimpleTutor.builder().build() : SimpleTutor.builder().id(tutor.getId()).fullName(tutor.getLastName() + " " + tutor.getFirstName()).build())
                .build();
    }
}
