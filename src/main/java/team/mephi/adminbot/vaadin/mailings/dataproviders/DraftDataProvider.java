package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.model.enums.MailingStatus;

import java.util.List;

public class DraftDataProvider extends BaseMailingDataProvider {

    public DraftDataProvider(MailingService mailingService) {
        super(mailingService);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return List.of(MailingStatus.DRAFT);
    }
}
