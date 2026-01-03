package team.mephi.adminbot.vaadin.questions.dataproviders;

public interface QuestionService {
    void saveAnswer(Long question, String user, String text);
}
