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

    public DashboardDataProvider(MessageService messageService, QuestionService questionService) {
        this.messageService = messageService;
        this.questionService = questionService;
    }

    public Integer unreadCount() {
        return messageService.unreadCount();
    }

    public Integer countNewQuestion() {
        return questionService.countNewQuestion();
    }
}
