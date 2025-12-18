package team.mephi.adminbot.vaadin.mailings.dataproviders;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.Arrays;
import java.util.List;

@Component("sent")
public class SentDataProvider extends BaseMailingDataProvider {

    public SentDataProvider(MailingRepository mailingRepository) {
        super(mailingRepository);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return Arrays.stream(MailingStatus.values()).filter(s -> s != MailingStatus.DRAFT).toList();
    }
}
