package team.mephi.adminbot.vaadin.questions.dataproviders;

import team.mephi.adminbot.dto.SimpleQuestion;

public interface QuestionService {
    SimpleQuestion saveAnswer(SimpleQuestion question);
}
