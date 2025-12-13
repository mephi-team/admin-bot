package team.mephi.adminbot.integration.neostudy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Класс с настройками интеграции с NeoStudy.
 *
 * Все значения подгружаются из application.properties или application.yml
 * с префиксом "neostudy".
 *
 * Пример настроек:
 *
 * neostudy.base-url=https://api.neostudy.example.com
 * neostudy.api-key=your-api-key-here
 * neostudy.timeout.connect=5s
 * neostudy.timeout.read=30s
 * neostudy.retry.max-attempts=3
 * neostudy.retry.backoff-delay=1s
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "neostudy")
public class NeoStudyProperties {

    /**
     * Базовый URL API NeoStudy.
     * Например: https://api.neostudy.example.com
     */
    private String baseUrl;

    /**
     * API-ключ для доступа к NeoStudy.
     * Может использоваться как Bearer-токен
     * или передаваться в отдельном заголовке.
     */
    private String apiKey;

    /**
     * URL для получения OAuth2-токена.
     * Используется, если вместо API-ключа применяется OAuth2.
     * Необязательное поле.
     */
    private String tokenUrl;

    /**
     * Client ID для OAuth2-аутентификации.
     * Необязательное поле.
     */
    private String clientId;

    /**
     * Client Secret для OAuth2-аутентификации.
     * Необязательное поле.
     */
    private String clientSecret;

    /**
     * Настройки таймаутов для HTTP-запросов.
     */
    private Timeout timeout = new Timeout();

    /**
     * Настройки повторных попыток при ошибках запросов.
     */
    private Retry retry = new Retry();

    /**
     * Флаг, включена ли интеграция с NeoStudy.
     * По умолчанию интеграция включена.
     */
    private boolean enabled = true;

    /**
     * Настройки таймаутов.
     */
    @Data
    public static class Timeout {

        /**
         * Таймаут на установку соединения.
         * По умолчанию — 5 секунд.
         */
        private Duration connect = Duration.ofSeconds(5);

        /**
         * Таймаут на ожидание ответа от сервера.
         * По умолчанию — 30 секунд.
         */
        private Duration read = Duration.ofSeconds(30);

        /**
         * Таймаут на отправку данных.
         * По умолчанию — 10 секунд.
         */
        private Duration write = Duration.ofSeconds(10);
    }

    /**
     * Настройки повторных попыток.
     */
    @Data
    public static class Retry {

        /**
         * Максимальное количество повторных попыток запроса.
         * По умолчанию — 3.
         */
        private int maxAttempts = 3;

        /**
         * Задержка перед первой повторной попыткой.
         * По умолчанию — 1 секунда.
         */
        private Duration backoffDelay = Duration.ofSeconds(1);

        /**
         * Множитель для экспоненциальной задержки.
         * Например: 1s → 2s → 4s.
         * По умолчанию — 2.0.
         */
        private double multiplier = 2.0;
    }
}
