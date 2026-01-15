package team.mephi.adminbot.vaadin.dashboard.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.MessageService;
import team.mephi.adminbot.service.QuestionService;

/**
 * Фабрика для создания экземпляров DashboardDataProvider.
 */
@SpringComponent
public class DashboardDataProviderFactory {
    private final MessageService messageService;
    private final QuestionService questionService;

    /**
     * Конструктор класса DashboardDataProviderFactory.
     *
     * @param messageService сервис для работы с сообщениями
     * @param questionService сервис для работы с вопросами
     */
    public DashboardDataProviderFactory(MessageService messageService, QuestionService questionService) {
        this.messageService = messageService;
        this.questionService = questionService;
    }

    /**
     * Создает новый экземпляр DashboardDataProvider.
     *
     * @return новый экземпляр DashboardDataProvider
     */
    public DashboardDataProvider create() {
        return new DashboardDataProvider(messageService, questionService);
    }
}
