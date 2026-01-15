package team.mephi.adminbot.vaadin.mailings.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.service.MailingService;
import team.mephi.adminbot.service.TemplateService;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabType;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.NotificationService;

/**
 * Фабрика для создания презентеров рассылок в зависимости от типа вкладки.
 */
@SpringComponent
public class MailingPresenterFactory {
    private final MailingService mailingService;
    private final TemplateService templateService;
    private final DialogService dialogService;
    private final NotificationService notificationService;

    /**
     * Конструктор фабрики презентеров рассылок.
     *
     * @param mailingService     сервис для работы с рассылками
     * @param templateService    сервис для работы с шаблонами
     * @param dialogService      сервис для отображения диалогов
     * @param notificationService сервис для отображения уведомлений
     */
    public MailingPresenterFactory(
            MailingService mailingService,
            TemplateService templateService,
            DialogService<?> dialogService,
            NotificationService notificationService) {
        this.mailingService = mailingService;
        this.templateService = templateService;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    /**
     * Создает провайдер данных в зависимости от типа вкладки рассылок.
     *
     * @param role тип вкладки рассылок
     * @return соответствующий провайдер данных
     */
    private CRUDDataProvider<?> createDataProvider(MailingTabType role) {
        return switch (role) {
            case SENT -> new SentDataProvider(mailingService);
            case DRAFT -> new DraftDataProvider(mailingService);
            case TEMPLATES -> new TemplateDataProvider(templateService);
        };
    }

    /**
     * Создает презентер в зависимости от типа вкладки рассылок.
     *
     * @param role тип вкладки рассылок
     * @return соответствующий презентер
     */
    @SuppressWarnings("unchecked")
    public CRUDPresenter<?> createPresenter(MailingTabType role) {
        CRUDDataProvider<?> dataProvider = createDataProvider(role);

        return switch (role) {
            case TEMPLATES ->
                    new CRUDPresenter<SimpleTemplate>((CRUDDataProvider<SimpleTemplate>) dataProvider, dialogService, notificationService);
            default ->
                    new MailingsPresenter((CRUDDataProvider<SimpleMailing>) dataProvider, dialogService, notificationService);
        };
    }
}
