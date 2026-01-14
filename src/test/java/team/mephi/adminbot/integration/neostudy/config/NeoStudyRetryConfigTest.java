package team.mephi.adminbot.integration.neostudy.config;

import org.junit.jupiter.api.Test;
import org.springframework.retry.annotation.EnableRetry;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для NeoStudyRetryConfig.
 * Проверяют наличие аннотации EnableRetry.
 */
class NeoStudyRetryConfigTest {

    /**
     * Проверяет наличие аннотации EnableRetry на конфигурации.
     */
    @Test
    void Given_retryConfig_When_checkAnnotations_Then_enableRetryIsPresent() {
        // Arrange

        // Act
        boolean hasAnnotation = NeoStudyRetryConfig.class.isAnnotationPresent(EnableRetry.class);

        // Assert
        assertTrue(hasAnnotation);
    }
}
