package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа от NeoStudy с информацией о записи пользователя на курс.
 *
 * Этот объект приходит от NeoStudy,
 * когда мы получаем данные о регистрации пользователя
 * или результат создания записи на курс.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyEnrollmentResponse {

    /**
     * Внутренний идентификатор записи на курс в NeoStudy.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Идентификатор пользователя в NeoStudy.
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * Идентификатор курса в NeoStudy.
     */
    @JsonProperty("course_id")
    private String courseId;

    /**
     * Текущий статус записи на курс.
     * Например: pending, active, completed, cancelled.
     */
    @JsonProperty("status")
    private String status;

    /**
     * Прогресс обучения по курсу.
     *
     * Значение от 0 до 100,
     * где 0 — обучение только началось,
     * а 100 — курс полностью пройден.
     */
    @JsonProperty("progress")
    private Integer progress;

    /**
     * Дата и время создания записи на курс в NeoStudy.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления записи.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Дата и время, когда пользователь был записан на курс.
     */
    @JsonProperty("enrollment_date")
    private LocalDateTime enrollmentDate;
}
