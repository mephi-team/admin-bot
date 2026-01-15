package team.mephi.adminbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserQuestion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления сущностями UserQuestion.
 */
@Repository
@SuppressWarnings("unused")
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    /**
     * Поиск всех пользовательских вопросов по тексту с пагинацией.
     *
     * @param query    строка запроса для поиска в тексте вопроса
     * @param pageable объект Pageable для пагинации результатов
     * @return список пользовательских вопросов, соответствующих критериям поиска
     */

    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.user LEFT JOIN FETCH q.direction LEFT JOIN FETCH q.answers WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserQuestion> findAllByText(String query, Pageable pageable);
    /**
     * Подсчет количества пользовательских вопросов, соответствующих заданному запросу по тексту.
     *
     * @param query строка запроса для поиска в тексте вопроса
     * @return количество пользовательских вопросов, соответствующих критериям поиска
     */

    @Query("SELECT count(q) FROM UserQuestion q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByText(String query);
    /**
     * Поиск всех пользовательских вопросов вместе с их ответами.
     *
     * @return список всех пользовательских вопросов с их ответами
     */
    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.answers")
    List<UserQuestion> findAllWithAnswers();

    /**
     * Подсчет количества новых пользовательских вопросов.
     *
     * @return количество новых пользовательских вопросов
     */
    @Query("SELECT count(q) FROM UserQuestion q WHERE q.status = team.mephi.adminbot.model.enums.QuestionStatus.NEW")
    Integer countNewQuestion();

    /**
     * Подсчет количества пользовательских вопросов, созданных после указанного времени.
     *
     * @param createdAt время создания для фильтрации вопросов
     * @return количество пользовательских вопросов, созданных после указанного времени
     */
    long countByCreatedAtAfter(Instant createdAt);

    /**
     * Поиск пользовательского вопроса по его идентификатору вместе с его зависимостями.
     *
     * @param id идентификатор пользовательского вопроса
     * @return опциональный пользовательский вопрос с его зависимостями
     */
    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.user JOIN FETCH q.direction LEFT JOIN FETCH q.answers WHERE q.id = :id")
    Optional<UserQuestion> findByIdWithDeps(Long id);
}

