package team.mephi.adminbot.integration.neostudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Конфигурация для включения механизма повторных попыток (retry).
 *
 * Включает поддержку аннотации @Retryable,
 * которая позволяет автоматически повторять
 * неудачные операции (например, запросы к NeoStudy).
 */
@Configuration
@EnableRetry
public class NeoStudyRetryConfig {

    // Логика не требуется — достаточно аннотации @EnableRetry,
    // чтобы Spring Retry начал работать
}
