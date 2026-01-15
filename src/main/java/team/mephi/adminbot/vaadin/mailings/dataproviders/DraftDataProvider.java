package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.service.MailingService;

import java.util.List;

/**
 * Провайдер данных для рассылок в статусе "Черновик".
 */
public class DraftDataProvider extends BaseMailingDataProvider {

    /**
     * Конструктор провайдера данных для черновиков.
     *
     * @param mailingService сервис для работы с рассылками
     */
    public DraftDataProvider(MailingService mailingService) {
        super(mailingService);
    }

    @Override
    protected List<MailingStatus> getStatuses() {
        return List.of(MailingStatus.DRAFT);
    }
}
