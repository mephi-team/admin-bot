package team.mephi.adminbot.integration.neostudy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyEnrollmentResponse;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyWebhookPayload;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyException;
import team.mephi.adminbot.integration.neostudy.service.NeoStudyService;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.DirectionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST-контроллер для интеграции с NeoStudy.
 * <p>
 * Здесь находятся эндпоинты для:
 * - регистрации пользователей в NeoStudy
 * - синхронизации пользователей
 * - синхронизации курсов (направлений)
 * - записи пользователей на курсы
 * - приёма вебхуков от NeoStudy
 * <p>
 * Все эндпоинты доступны только администраторам (ROLE_ADMIN).
 */
@Slf4j
@RestController
@RequestMapping("/api/neostudy")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NeoStudyController {

    private final NeoStudyService neoStudyService;
    private final UserRepository userRepository;
    private final DirectionRepository directionRepository;

    /**
     * POST /api/neostudy/users/{userId}/register
     * <p>
     * Регистрирует пользователя из нашей базы в системе NeoStudy.
     */
    @PostMapping("/users/{userId}/register")
    public ResponseEntity<Map<String, Object>> registerUser(@PathVariable Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                // Пользователь не найден
                return ResponseEntity.notFound().build();
            }

            User user = neoStudyService.registerUser(userOpt.get());

            // Формируем ответ об успешной регистрации
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getId());
            response.put("neostudyExternalId", user.getNeostudyExternalId());
            response.put("syncedAt", user.getNeostudySyncedAt());

            return ResponseEntity.ok(response);
        } catch (NeoStudyException e) {
            log.error("Ошибка регистрации пользователя в NeoStudy: userId={}", userId, e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    /**
     * POST /api/neostudy/users/{userId}/sync
     * <p>
     * Синхронизирует данные пользователя с NeoStudy
     * (обновляет информацию в NeoStudy или у нас).
     */
    @PostMapping("/users/{userId}/sync")
    public ResponseEntity<Map<String, Object>> syncUser(@PathVariable Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = neoStudyService.syncUser(userOpt.get());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getId());
            response.put("neostudyExternalId", user.getNeostudyExternalId());
            response.put("syncedAt", user.getNeostudySyncedAt());

            return ResponseEntity.ok(response);
        } catch (NeoStudyException e) {
            log.error("Ошибка синхронизации пользователя с NeoStudy: userId={}", userId, e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    /**
     * POST /api/neostudy/courses/sync
     * <p>
     * Загружает курсы из NeoStudy и синхронизирует их
     * с нашей базой направлений.
     */
    @PostMapping("/courses/sync")
    public ResponseEntity<Map<String, Object>> syncCourses() {
        try {
            List<Direction> directions = neoStudyService.syncCourses();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("syncedCount", directions.size());
            response.put(
                    "directions",
                    directions.stream()
                            .map(d -> Map.of(
                                    "id", d.getId(),
                                    "code", d.getCode(),
                                    "name", d.getName(),
                                    "neostudyExternalId",
                                    d.getNeostudyExternalId() != null
                                            ? d.getNeostudyExternalId()
                                            : "N/A"
                            ))
                            .toList()
            );

            return ResponseEntity.ok(response);
        } catch (NeoStudyException e) {
            log.error("Ошибка синхронизации курсов с NeoStudy", e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    /**
     * POST /api/neostudy/enrollments
     * <p>
     * Записывает пользователя на курс в NeoStudy.
     * <p>
     * В теле запроса ожидаются:
     * - userId — ID пользователя
     * - directionId — ID направления (курса)
     * - status — статус записи (по умолчанию "active")
     */
    @PostMapping("/enrollments")
    public ResponseEntity<Map<String, Object>> createEnrollment(
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long directionId = Long.valueOf(request.get("directionId").toString());
            String status = request.getOrDefault("status", "active").toString();

            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Direction> directionOpt = directionRepository.findById(directionId);

            // Проверяем, что пользователь и курс существуют
            if (userOpt.isEmpty() || directionOpt.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Пользователь или направление не найдены");
                return ResponseEntity.badRequest().body(error);
            }

            NeoStudyEnrollmentResponse enrollment =
                    neoStudyService.createEnrollment(
                            userOpt.get(),
                            directionOpt.get(),
                            status
                    );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            if (enrollment != null) {
                response.put("enrollmentId", enrollment.getId());
                response.put("status", enrollment.getStatus());
                response.put("progress", enrollment.getProgress());
            }

            return ResponseEntity.ok(response);
        } catch (NeoStudyException e) {
            log.error("Ошибка создания записи на курс в NeoStudy", e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    /**
     * POST /api/neostudy/webhooks
     * <p>
     * Принимает вебхуки от NeoStudy.
     * <p>
     * NeoStudy присылает сюда события,
     * например изменение статуса записи на курс.
     * <p>
     * В продакшене рекомендуется добавить
     * проверку подписи вебхука.
     */
    @PostMapping("/webhooks")
    public ResponseEntity<Map<String, Object>> receiveWebhook(
            @RequestBody NeoStudyWebhookPayload payload) {
        try {
            log.info(
                    "Получен вебхук от NeoStudy: eventType={}",
                    payload.getEventType()
            );

            neoStudyService.processWebhook(payload);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Вебхук успешно обработан");

            return ResponseEntity.ok(response);
        } catch (NeoStudyException e) {
            log.error("Ошибка обработки вебхука от NeoStudy", e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    /**
     * GET /api/neostudy/health
     * <p>
     * Простой эндпоинт для проверки,
     * что интеграция с NeoStudy работает.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("integration", "NeoStudy");
        return ResponseEntity.ok(health);
    }
}
