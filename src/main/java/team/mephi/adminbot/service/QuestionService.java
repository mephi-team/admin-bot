package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleQuestion;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления вопросами.
 */
public interface QuestionService {
    /**
     * Сохраняет ответ на вопрос.
     *
     * @param question объект SimpleQuestion для сохранения.
     * @return сохраненный объект SimpleQuestion.
     */
    SimpleQuestion saveAnswer(SimpleQuestion question);

    /**
     * Находит вопросы по тексту с пагинацией.
     *
     * @param name     текст вопроса.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток вопросов, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleQuestion> findAllByText(String name, Pageable pageable);

    /**
     * Подсчитывает количество вопросов по тексту.
     *
     * @param name текст вопроса.
     * @return количество вопросов, соответствующих критериям поиска.
     */
    Integer countByText(String name);

    /**
     * Находит вопрос по его идентификатору вместе с зависимостями.
     *
     * @param id Идентификатор вопроса.
     * @return Опциональный вопрос, если он существует.
     */
    Optional<SimpleQuestion> findByIdWithDeps(Long id);

    /**
     * Удаляет все вопросы по их идентификаторам.
     *
     * @param ids коллекция идентификаторов вопросов для удаления.
     */
    void deleteAllById(Iterable<Long> ids);

    /**
     * Подсчитывает количество новых вопросов.
     *
     * @return Количество новых вопросов.
     */
    Integer countNewQuestion();
}
