package team.mephi.adminbot.vaadin.mailings.dataproviders;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.List;

@Component("draft")
public class DraftDataProvider extends BaseMailingDataProvider {

    public DraftDataProvider(MailingRepository mailingRepository) {
        super(mailingRepository);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return List.of(MailingStatus.DRAFT);
    }
}
