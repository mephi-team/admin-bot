package team.mephi.adminbot.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.service.QuestionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для работы эксперта LC.
 * <p>
 * Все методы этого контроллера доступны только пользователям
 * с ролью ROLE_LC_EXPERT.
 * <p>
 * Доступ ограничен с помощью аннотации
 */
@RestController
@RequestMapping("/api/expert")
@PreAuthorize("hasRole('LC_EXPERT')")
@AllArgsConstructor
public class ExpertController {

    // Репозиторий для работы с вопросами в базе данных
    private QuestionService questionService;

    /**
     * GET /api/expert/questions
     * <p>
     * Возвращает список всех вопросов.
     * Доступно только эксперту.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<SimpleQuestion>> getAllQuestions() {
        // Получаем все вопросы из базы данных
        List<SimpleQuestion> questions = questionService.findAllByText("", Pageable.unpaged()).toList();

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
    public ResponseEntity<SimpleQuestion> getQuestionById(@PathVariable Long id) {
        return questionService.findByIdWithDeps(id)
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
    public ResponseEntity<SimpleQuestion> updateQuestion(
            @PathVariable Long id,
            @RequestBody SimpleQuestion question) {

        return questionService.findByIdWithDeps(id)
                .map(existingQuestion -> {

                    // Если передан новый текст ответа — обновляем его
                    if (question.getAnswer() != null) {
                        existingQuestion.setAnswer(question.getAnswer());
                    }

                    // Сохраняем обновлённый вопрос в базе
                    SimpleQuestion updated = questionService.saveAnswer(existingQuestion);

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
        stats.put("totalQuestions", questionService.countNewQuestion());

        // Текущее время на сервере (в миллисекундах)
        stats.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(stats);
    }
}
