package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.Arrays;
import java.util.List;

public class SentDataProvider extends BaseMailingDataProvider {

    public SentDataProvider(MailingService mailingService, MailingRepository mailingRepository) {
        super(mailingService, mailingRepository);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return Arrays.stream(MailingStatus.values()).filter(s -> s != MailingStatus.DRAFT).toList();
    }
}
