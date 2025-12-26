package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.MailTemplateRepository;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;

@SpringComponent
public class MailingPresenterFactory {
    private final MailingRepository mailingRepository;
    private final UserRepository userRepository;
    private final MailTemplateRepository mailTemplateRepository;

    public MailingPresenterFactory(
            MailingRepository mailingRepository,
            UserRepository userRepository,
            MailTemplateRepository mailTemplateRepository) {
        this.mailingRepository = mailingRepository;
        this.userRepository = userRepository;
        this.mailTemplateRepository = mailTemplateRepository;
    }

    public MailingDataProvider<?> createDataProvider(String role) {
        return switch (role) {
            case "sent" -> new SentDataProvider(mailingRepository, userRepository);
            case "draft" -> new DraftDataProvider(mailingRepository, userRepository);
            case "templates" -> new TemplateDataProvider(mailTemplateRepository);
            default -> throw new IllegalArgumentException("Unknown provider: " + role);
        };
    }
}
