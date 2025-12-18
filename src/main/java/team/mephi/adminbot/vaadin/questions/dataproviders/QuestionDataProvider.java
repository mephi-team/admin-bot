package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Component;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.repository.UserQuestionRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component("question")
public class QuestionDataProvider {
    private final  UserQuestionRepository questionRepository;
    private ConfigurableFilterDataProvider<UserQuestionDto, Void, String> provider;

    public QuestionDataProvider(UserQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public ConfigurableFilterDataProvider<UserQuestionDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<UserQuestionDto, String>(
                    query -> questionRepository.findAllByText(query.getFilter().orElse(""))
                                .stream()
                                .map(u -> UserQuestionDto
                                                .builder()
                                                .id(u.getId())
                                                .question(u.getText())
                                                .date(LocalDateTime.ofInstant(u.getCreatedAt(), ZoneId.of("UTC")))
                                                .user(u.getUser().getUserName())
                                                .role(u.getRole())
//                                    .direction(u.getDirection() != null ? u.getDirection().getName() : "")
                                                .answer(!u.getAnswers().isEmpty() ? u.getAnswers().getLast().getAnswerText() : "")
                                                .build()
                                )
                                .skip(query.getOffset())
                                .limit(query.getLimit()),
                    query -> questionRepository.countByText(query.getFilter().orElse("")),
                    UserQuestionDto::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    public DataProvider<UserQuestionDto, ?> getDataProvider() {
        return getFilterableProvider();
    }

    public void deleteAllById(Iterable<Long> ids) {
        questionRepository.deleteAllById(ids);
    }

    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }
}
