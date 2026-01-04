package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.enums.Channels;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.model.objects.Filters;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingService;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MailingServiceImpl implements MailingService {
    private final AuthenticationContext authContext;
    private final MailingRepository mailingRepository;
    private final UserRepository userRepository;

    public MailingServiceImpl(AuthenticationContext authContext, MailingRepository mailingRepository, UserRepository userRepository) {
        this.authContext = authContext;
        this.mailingRepository = mailingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SimpleMailing save(SimpleMailing mailing) {
        var user = authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();

        var result = mailing.getId() != null
                ? mailingRepository.findById(mailing.getId()).orElse(new Mailing())
                : new Mailing();
        result.setName(mailing.getName());
        result.setDescription(mailing.getText());
        if (Objects.isNull(result.getCreatedBy()))
            result.setCreatedBy(userRepository.findByEmail(user.getUserInfo().getEmail()).orElseThrow());
        result.setFilters(Filters.builder().curator(mailing.getCurator()).direction(mailing.getDirection()).city(mailing.getCity()).users(mailing.getUsers()).cohort(mailing.getCohort()).build());
        result.setStatus(Objects.isNull(mailing.getStatus()) ? MailingStatus.DRAFT : MailingStatus.valueOf(mailing.getStatus()));
        result.setChannels(mailing.getChannels().stream().map(Channels::valueOf).toList());
        result = mailingRepository.save(result);

        return SimpleMailing.builder()
                .id(result.getId())
                .date(result.getCreatedAt())
                .users(result.getFilters() != null ? result.getFilters().getUsers() : "")
                .cohort(result.getFilters() != null ? result.getFilters().getCohort() : "")
                .direction(result.getFilters() != null ? result.getFilters().getDirection() : "")
                .curator(result.getFilters() != null ? result.getFilters().getCurator() : "")
                .channels(result.getChannels().stream().map(Enum::name).collect(Collectors.toSet()))
                .city(result.getFilters() != null ? result.getFilters().getCity() : "")
                .text(result.getDescription())
                .status(result.getStatus().name())
                .build();
    }

    @Override
    @Transactional
    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }
}
