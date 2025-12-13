package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа от NeoStudy с информацией о курсе (направлении).
 *
 * Этот объект приходит от NeoStudy при получении списка курсов
 * или данных по конкретному курсу.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyCourseResponse {

    /**
     * Внутренний идентификатор курса в NeoStudy.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Короткий код курса.
     * Например: "CS101", "MATH201".
     */
    @JsonProperty("code")
    private String code;

    /**
     * Название курса.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Описание курса.
     * Обычно используется для отображения в интерфейсе.
     */
    @JsonProperty("description")
    private String description;

    /**
     * Текущий статус курса.
     * Например: "active", "inactive", "archived".
     */
    @JsonProperty("status")
    private String status;

    /**
     * Дата и время создания курса в NeoStudy.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления курса.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
