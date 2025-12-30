package team.mephi.adminbot.vaadin.mailings.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
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

    private CRUDDataProvider<?> createDataProvider(String role) {
        return switch (role) {
            case "sent" -> new SentDataProvider(mailingRepository, userRepository);
            case "draft" -> new DraftDataProvider(mailingRepository, userRepository);
            case "templates" -> new TemplateDataProvider(mailTemplateRepository);
            default -> throw new IllegalArgumentException("Unknown provider: " + role);
        };
    }

    public CRUDPresenter<?> createPresenter(String role, CRUDViewCallback<?> view) {
        CRUDDataProvider<?> dataProvider = createDataProvider(role);

        return switch (role) {
            case "templates" -> new TemplatePresenter((CRUDDataProvider<SimpleTemplate>) dataProvider, (CRUDViewCallback<SimpleTemplate>) view);
            default -> new MailingsPresenter((CRUDDataProvider<SimpleMailing>) dataProvider, (MailingViewCallback) view);
        };
    }
}
