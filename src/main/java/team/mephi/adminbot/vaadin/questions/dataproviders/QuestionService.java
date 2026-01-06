package team.mephi.adminbot.vaadin.questions.dataproviders;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleQuestion;

import java.util.Optional;
import java.util.stream.Stream;

public interface QuestionService {
    SimpleQuestion saveAnswer(SimpleQuestion question);
    Stream<SimpleQuestion> findAllByText(String name, Pageable pageable);
    Integer countByText(String name);
    Optional<SimpleQuestion> findByIdWithDeps(Long id);
    void deleteAllById(Iterable<Long> ids);
}
