package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.enums.Channels;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.model.objects.Filters;
import team.mephi.adminbot.model.objects.ReasonCode;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация сервиса для управления рассылками.
 */
@Service
public class MailingServiceImpl implements MailingService {
    private final AuthService authService;
    private final MailingRepository mailingRepository;
    private final UserRepository userRepository;

    public MailingServiceImpl(AuthService authService, MailingRepository mailingRepository, UserRepository userRepository) {
        this.authService = authService;
        this.mailingRepository = mailingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SimpleMailing save(SimpleMailing mailing) {
        var result = mailing.getId() != null
                ? mailingRepository.findById(mailing.getId()).orElse(new Mailing())
                : new Mailing();
        result.setName(mailing.getName());
        result.setDescription(mailing.getText());
        if (Objects.isNull(result.getCreatedBy()))
            result.setCreatedBy(userRepository.findByEmail(authService.getUserInfo().getEmail()).orElseThrow());
        result.setFilters(Filters.builder().curator(mailing.getCurator()).direction(mailing.getDirection()).city(mailing.getCity()).users(mailing.getUsers()).cohort(mailing.getCohort()).build());
        result.setStatus(Objects.isNull(mailing.getStatus()) ? MailingStatus.DRAFT : MailingStatus.valueOf(mailing.getStatus()));
        result.setChannels(mailing.getChannels().stream().map(Channels::valueOf).toList());
        result.setReasonCode(ReasonCode.builder().users(mailing.getRecipients()).build());
        result = mailingRepository.save(result);

        return mapToSimple(result);
    }

    @Override
    public Optional<SimpleMailing> findById(Long id) {
        return mailingRepository.findById(id)
                .map(this::mapToSimple);
    }

    @Override
    public Stream<SimpleMailing> findMailingByName(String name, List<String> statuses, Pageable pageable) {
        return mailingRepository.findMailingByName(name, statuses, pageable)
                .stream()
                .map(this::mapToSimple);
    }

    @Override
    public Integer countByName(String name, List<MailingStatus> statuses) {
        return mailingRepository.countByName(name, statuses);
    }

    @Override
    @Transactional
    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }

    private SimpleMailing mapToSimple(Mailing mailing) {
        return SimpleMailing.builder()
                .id(mailing.getId())
                .date(mailing.getCreatedAt())
                .users(mailing.getFilters() != null ? mailing.getFilters().getUsers() : "")
                .cohort(mailing.getFilters() != null ? mailing.getFilters().getCohort() : "")
                .direction(mailing.getFilters() != null ? mailing.getFilters().getDirection() : "")
                .curator(mailing.getFilters() != null ? mailing.getFilters().getCurator() : "")
                .city(mailing.getFilters() != null ? mailing.getFilters().getCity() : "")
                .channels(mailing.getChannels().stream().map(Enum::name).collect(Collectors.toSet()))
                .recipients(mailing.getReasonCode() != null ? mailing.getReasonCode().getUsers() : List.of())
                .name(mailing.getName())
                .text(mailing.getDescription())
                .status(mailing.getStatus().name())
                .build();
    }
}
