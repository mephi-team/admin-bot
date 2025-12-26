package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;

public class DraftDataProvider extends BaseMailingDataProvider {

    public DraftDataProvider(MailingRepository mailingRepository, UserRepository userRepository) {
        super(mailingRepository, userRepository);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return List.of(MailingStatus.DRAFT);
    }
}
