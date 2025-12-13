package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO для ответа с ошибкой от API NeoStudy.
 *
 * Этот объект приходит от NeoStudy,
 * когда запрос не удалось обработать
 * (ошибка в данных, правах доступа или на стороне сервиса).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyErrorResponse {

    /**
     * Код ошибки.
     *
     * Используется для программной обработки ошибок
     * и понимания типа проблемы.
     */
    @JsonProperty("error_code")
    private String errorCode;

    /**
     * Краткое сообщение об ошибке.
     *
     * Обычно подходит для отображения пользователю
     * или логирования.
     */
    @JsonProperty("message")
    private String message;

    /**
     * Подробное описание ошибки.
     *
     * Может содержать технические детали,
     * полезные для отладки.
     */
    @JsonProperty("details")
    private String details;

    /**
     * Список ошибок валидации.
     *
     * Используется, если запрос не прошёл проверку
     * по нескольким полям сразу.
     */
    @JsonProperty("validation_errors")
    private List<ValidationError> validationErrors;

    /**
     * Дополнительные данные об ошибке.
     *
     * Может содержать служебную информацию,
     * ID запроса или другие метаданные.
     */
    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    /**
     * Описание одной ошибки валидации.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {

        /**
         * Поле, в котором произошла ошибка.
         */
        @JsonProperty("field")
        private String field;

        /**
         * Сообщение с описанием ошибки для этого поля.
         */
        @JsonProperty("message")
        private String message;
    }
}
