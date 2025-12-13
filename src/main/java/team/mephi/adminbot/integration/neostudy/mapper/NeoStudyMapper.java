package team.mephi.adminbot.integration.neostudy.mapper;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.integration.neostudy.dto.*;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Маппер для преобразования данных между нашей системой и NeoStudy.
 *
 * Отвечает за два направления:
 * - наши доменные модели → DTO NeoStudy (для отправки запросов)
 * - DTO NeoStudy → наши доменные модели (для обработки ответов)
 */
@Component
public class NeoStudyMapper {

    /**
     * Преобразует нашего пользователя в DTO,
     * которое отправляется в NeoStudy.
     */
    public NeoStudyUserRequest toNeoStudyUserRequest(User user) {
        if (user == null) {
            return null;
        }

        return NeoStudyUserRequest.builder()
                .externalId(user.getExternalId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(user.getStatus())
                .build();
    }

    /**
     * Преобразует ответ от NeoStudy в объект User.
     *
     * Важно: здесь создаётся новый объект User.
     * Если нужно обновлять существующего пользователя —
     * используйте отдельный метод updateUserFromNeoStudy.
     */
    public User toUser(NeoStudyUserResponse response) {
        if (response == null) {
            return null;
        }

        return User.builder()
                .externalId(response.getExternalId())
                .name(response.getName())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .status(response.getStatus())
                // Если даты не пришли — ставим текущее время
                .createdAt(response.getCreatedAt() != null
                        ? response.getCreatedAt()
                        : LocalDateTime.now())
                .updatedAt(response.getUpdatedAt() != null
                        ? response.getUpdatedAt()
                        : LocalDateTime.now())
                .build();
    }

    /**
     * Обновляет существующего пользователя данными,
     * которые пришли от NeoStudy.
     *
     * Обновляются только те поля,
     * которые реально присутствуют в ответе.
     */
    public void updateUserFromNeoStudy(User user, NeoStudyUserResponse response) {
        if (user == null || response == null) {
            return;
        }

        if (response.getName() != null) {
            user.setName(response.getName());
        }
        if (response.getFirstName() != null) {
            user.setFirstName(response.getFirstName());
        }
        if (response.getLastName() != null) {
            user.setLastName(response.getLastName());
        }
        if (response.getStatus() != null) {
            user.setStatus(response.getStatus());
        }
        if (response.getUpdatedAt() != null) {
            user.setUpdatedAt(response.getUpdatedAt());
        }
    }

    /**
     * Преобразует наше направление (Direction)
     * в DTO курса NeoStudy.
     *
     * Обычно курсы приходят от NeoStudy,
     * но этот метод может быть полезен
     * для сравнения или проверки данных.
     */
    public NeoStudyCourseResponse toNeoStudyCourse(Direction direction) {
        if (direction == null) {
            return null;
        }

        return NeoStudyCourseResponse.builder()
                .code(direction.getCode())
                .name(direction.getName())
                .build();
    }

    /**
     * Преобразует курс, полученный от NeoStudy,
     * в нашу доменную модель Direction.
     */
    public Direction toDirection(NeoStudyCourseResponse response) {
        if (response == null) {
            return null;
        }

        return Direction.builder()
                .code(response.getCode())
                .name(response.getName())
                .build();
    }

    /**
     * Обновляет существующее направление
     * данными, полученными от NeoStudy.
     */
    public void updateDirectionFromNeoStudy(Direction direction, NeoStudyCourseResponse response) {
        if (direction == null || response == null) {
            return;
        }

        if (response.getName() != null) {
            direction.setName(response.getName());
        }
        if (response.getCode() != null) {
            direction.setCode(response.getCode());
        }
    }

    /**
     * Создаёт запрос на запись пользователя на курс в NeoStudy.
     *
     * Используется, когда нужно просто записать пользователя
     * с указанным статусом.
     */
    public NeoStudyEnrollmentRequest toNeoStudyEnrollmentRequest(
            String userId,
            String courseId,
            String status) {

        return NeoStudyEnrollmentRequest.builder()
                .userId(userId)
                .courseId(courseId)
                .status(status)
                // Дата записи — текущее время
                .enrollmentDate(LocalDateTime.now().toString())
                .build();
    }

    /**
     * Создаёт запрос на запись пользователя на курс
     * с дополнительными метаданными.
     */
    public NeoStudyEnrollmentRequest toNeoStudyEnrollmentRequest(
            String userId,
            String courseId,
            String status,
            Map<String, Object> metadata) {

        return NeoStudyEnrollmentRequest.builder()
                .userId(userId)
                .courseId(courseId)
                .status(status)
                .enrollmentDate(LocalDateTime.now().toString())
                // Если метаданные не передали — используем пустую map
                .metadata(metadata != null ? metadata : new HashMap<>())
                .build();
    }
}
