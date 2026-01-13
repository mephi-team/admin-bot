package team.mephi.adminbot.integration.neostudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO для данных вебхуков, которые присылает NeoStudy.
 * <p>
 * Этот объект используется для приёма callback-запросов
 * от NeoStudy, когда в системе происходят события
 * (например, изменение статуса записи на курс).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeoStudyWebhookPayload {

    /**
     * Тип события.
     * <p>
     * Например:
     * - "enrollment.created" — пользователь записан на курс
     * - "enrollment.updated" — обновился статус записи
     * - "user.updated" — изменились данные пользователя
     */
    @JsonProperty("event_type")
    private String eventType;

    /**
     * Дата и время, когда произошло событие.
     */
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    /**
     * Основные данные события.
     * <p>
     * Содержимое зависит от типа события и может включать:
     * пользователя, запись на курс, курс и т.д.
     */
    @JsonProperty("data")
    private Map<String, Object> data;

    /**
     * Подпись вебхука.
     * <p>
     * Может использоваться для проверки,
     * что запрос действительно пришёл от NeoStudy.
     * Поле необязательное.
     */
    @JsonProperty("signature")
    private String signature;
}
