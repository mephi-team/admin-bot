package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserAnswer;

/**
 * Репозиторий для управления сущностями {@link UserAnswer}.
 *
 * <p>Наследует стандартные CRUD-операции и методы пагинации/сортировки от {@link JpaRepository}.</p>
 *
 * <p>Содержит дополнительный JPQL-запрос для подсчёта ответов, связанных с конкретным вопросом.</p>
 */
@Repository
@SuppressWarnings("unused")
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    /**
     * Выполняет подсчёт записей {@code UserAnswer}, связанных с вопросом по его идентификатору.
     *
     * <p>Используется кастомный JPQL-запрос, чтобы вернуть количество ответов для вопроса.</p>
     *
     * @param id идентификатор вопроса, по которому нужно посчитать ответы
     * @return количество найденных записей {@code UserAnswer} для заданного вопроса (тип {@link Integer})
     */
    @Query("SELECT count(a) FROM  UserAnswer a WHERE a.question.id = :id")
    Integer countByQuestionId(Long id);
}

