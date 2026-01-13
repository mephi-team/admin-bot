package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа от API NeoStudy с информацией о пользователе.
 * <p>
 * Этот объект приходит от NeoStudy в ответ на запросы
 * создания, обновления или получения пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyUserResponse {

    /**
     * Внутренний идентификатор пользователя в NeoStudy.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Внешний идентификатор пользователя из нашей системы.
     * <p>
     * Используется для связи пользователя NeoStudy
     * с пользователем в нашей базе.
     */
    @JsonProperty("external_id")
    private String externalId;

    /**
     * Полное имя пользователя.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Имя пользователя.
     */
    @JsonProperty("first_name")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @JsonProperty("last_name")
    private String lastName;

    /**
     * Email пользователя.
     */
    @JsonProperty("email")
    private String email;

    /**
     * Текущий статус пользователя в NeoStudy
     * (например: active, blocked, inactive).
     */
    @JsonProperty("status")
    private String status;

    /**
     * Дата и время, когда пользователь был создан в NeoStudy.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления пользователя в NeoStudy.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
