package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания или обновления записи пользователя на курс в NeoStudy.
 *
 * Этот объект отправляется в API NeoStudy,
 * когда нужно записать пользователя на курс
 * или изменить статус его обучения.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyEnrollmentRequest {

    /**
     * Идентификатор пользователя в NeoStudy.
     *
     * Может быть внутренний ID NeoStudy
     * или внешний ID из нашей системы — в зависимости от API.
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * Идентификатор курса в NeoStudy.
     *
     * Это может быть внутренний ID курса
     * или его код (например "CS101").
     */
    @JsonProperty("course_id")
    private String courseId;

    /**
     * Статус записи пользователя на курс.
     *
     * Например:
     * - "pending" — ожидает подтверждения
     * - "active" — обучение активно
     * - "completed" — курс завершён
     * - "cancelled" — запись отменена
     */
    @JsonProperty("status")
    private String status;

    /**
     * Дата записи пользователя на курс.
     *
     * Обычно передаётся в строковом формате,
     * который ожидает API NeoStudy.
     */
    @JsonProperty("enrollment_date")
    private String enrollmentDate;

    /**
     * Дополнительные данные по записи.
     *
     * Можно использовать для передачи
     * служебной или кастомной информации.
     */
    @JsonProperty("metadata")
    private java.util.Map<String, Object> metadata;
}
