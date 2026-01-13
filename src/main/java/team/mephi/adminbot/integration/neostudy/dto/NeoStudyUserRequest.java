package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания или обновления пользователя в NeoStudy.
 * <p>
 * Этот объект используется как тело запроса,
 * которое отправляется в API NeoStudy при создании
 * или обновлении информации о пользователе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyUserRequest {

    /**
     * Внешний идентификатор пользователя из нашей системы.
     * <p>
     * По этому ID NeoStudy понимает,
     * какой пользователь у них соответствует нашему.
     */
    @JsonProperty("external_id")
    private String externalId;

    /**
     * Полное имя пользователя.
     * Обычно отображается в интерфейсе NeoStudy.
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
     * Используется для уведомлений и идентификации.
     */
    @JsonProperty("email")
    private String email;

    /**
     * Статус пользователя.
     * Например: "active", "blocked", "inactive".
     */
    @JsonProperty("status")
    private String status;

    /**
     * Дополнительные данные о пользователе.
     * <p>
     * Можно передавать любые произвольные поля,
     * которые NeoStudy умеет обрабатывать
     * (например: роли, теги, настройки и т.д.).
     */
    @JsonProperty("metadata")
    private java.util.Map<String, Object> metadata;
}

