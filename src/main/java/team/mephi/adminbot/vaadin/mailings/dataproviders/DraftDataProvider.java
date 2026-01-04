package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.List;

public class DraftDataProvider extends BaseMailingDataProvider {

    public DraftDataProvider(MailingService mailingService, MailingRepository mailingRepository) {
        super(mailingService, mailingRepository);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return List.of(MailingStatus.DRAFT);
    }
}
