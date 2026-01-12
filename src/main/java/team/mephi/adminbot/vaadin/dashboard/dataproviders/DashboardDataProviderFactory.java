package team.mephi.adminbot.vaadin.dashboard.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.MessageService;
import team.mephi.adminbot.service.QuestionService;

@SpringComponent
public class DashboardDataProviderFactory {
    private final MessageService messageService;
    private final QuestionService questionService;

    public DashboardDataProviderFactory(MessageService messageService, QuestionService questionService) {
        this.messageService = messageService;
        this.questionService = questionService;
    }

    public DashboardDataProvider create() {
        return new DashboardDataProvider(messageService, questionService);
    }
}
