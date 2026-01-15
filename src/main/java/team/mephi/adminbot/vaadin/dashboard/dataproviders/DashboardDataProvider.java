package team.mephi.adminbot.vaadin.dashboard.dataproviders;

import team.mephi.adminbot.service.MessageService;
import team.mephi.adminbot.service.QuestionService;

/**
 * Провайдер данных для панели управления.
 * Предоставляет методы для получения количества непрочитанных сообщений и новых вопросов.
 */
public class DashboardDataProvider {
    private final MessageService messageService;
    private final QuestionService questionService;

    /**
     * Конструктор класса DashboardDataProvider.
     *
     * @param messageService сервис для работы с сообщениями
     * @param questionService сервис для работы с вопросами
     */
    public DashboardDataProvider(MessageService messageService, QuestionService questionService) {
        this.messageService = messageService;
        this.questionService = questionService;
    }

    /**
     * Получает количество непрочитанных сообщений.
     *
     * @return количество непрочитанных сообщений
     */
    public Integer unreadCount() {
        return messageService.unreadCount();
    }

    /**
     * Получает количество новых вопросов.
     *
     * @return количество новых вопросов
     */
    public Integer countNewQuestion() {
        return questionService.countNewQuestion();
    }
}
