package team.mephi.adminbot.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.QuestionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для работы эксперта LC.
 * <p>
 * Все методы этого контроллера доступны только пользователям
 * с ролью ROLE_LC_EXPERT.
 * <p>
 * Доступ ограничен с помощью аннотации:
 *
 * @PreAuthorize("hasRole('LC_EXPERT')")
 */
@RestController
@RequestMapping("/api/expert")
@PreAuthorize("hasRole('LC_EXPERT')")
public class ExpertController {

    // Репозиторий для работы с вопросами в базе данных
    @Autowired
    private QuestionRepository questionRepository;

    /**
     * GET /api/expert/questions
     * <p>
     * Возвращает список всех вопросов.
     * Доступно только эксперту.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        // Получаем все вопросы из базы данных
        List<Question> questions = questionRepository.findAll();

        // Отдаём список вопросов клиенту
        return ResponseEntity.ok(questions);
    }

    /**
     * GET /api/expert/questions/{id}
     * <p>
     * Возвращает один конкретный вопрос по его ID.
     * Если вопрос не найден — вернётся 404.
     * Доступно только эксперту.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                // Если вопрос найден — возвращаем его
                .map(ResponseEntity::ok)
                // Если нет — возвращаем 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/expert/questions/{id}
     * <p>
     * Обновляет данные вопроса.
     * Можно изменить текст вопроса и/или текст ответа.
     * <p>
     * Обновляются только те поля, которые пришли в запросе.
     * Доступно только эксперту.
     */
    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question question) {

        return questionRepository.findById(id)
                .map(existingQuestion -> {

                    // Если передан новый текст вопроса — обновляем его
                    if (question.getQuestionText() != null) {
                        existingQuestion.setQuestionText(question.getQuestionText());
                    }

                    // Если передан новый текст ответа — обновляем его
                    if (question.getAnswerText() != null) {
                        existingQuestion.setAnswerText(question.getAnswerText());
                    }

                    // Сохраняем обновлённый вопрос в базе
                    Question updated = questionRepository.save(existingQuestion);

                    // Возвращаем обновлённый объект
                    return ResponseEntity.ok(updated);
                })
                // Если вопрос с таким ID не найден — возвращаем 404
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/expert/stats
     * <p>
     * Возвращает простую статистику для экрана эксперта:
     * - общее количество вопросов
     * - текущее время на сервере
     * <p>
     * Доступно только эксперту.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getExpertStats() {
        Map<String, Object> stats = new HashMap<>();

        // Общее количество вопросов в системе
        stats.put("totalQuestions", questionRepository.count());

        // Текущее время на сервере (в миллисекундах)
        stats.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(stats);
    }
}
