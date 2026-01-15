package team.mephi.adminbot.integration.neostudy.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import team.mephi.adminbot.integration.neostudy.config.NeoStudyProperties;
import team.mephi.adminbot.integration.neostudy.dto.NeoStudyErrorResponse;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyClientException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyServerException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyTimeoutException;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для NeoStudyClient.
 * Проверяют преобразование ошибок в специализированные исключения.
 */
@ExtendWith(MockitoExtension.class)
class NeoStudyClientTest {
    @Mock
    private WebClient webClient;
    @Mock
    private NeoStudyProperties properties;

    private NeoStudyClient client;

    @BeforeEach
    void setUp() {
        client = new NeoStudyClient(webClient, properties, new ObjectMapper());
    }

    /**
     * Проверяет преобразование 4xx в NeoStudyClientException.
     */
    @Test
    void Given_clientErrorResponse_When_mapToNeoStudyException_Then_returnsClientException() throws Exception {
        // Arrange
        String payload = new ObjectMapper()
                .writeValueAsString(NeoStudyErrorResponse.builder().message("Неверные данные").build());
        WebClientResponseException exception = WebClientResponseException.create(
                400,
                "Bad Request",
                HttpHeaders.EMPTY,
                payload.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertInstanceOf(NeoStudyClientException.class, result);
        assertEquals(400, ((NeoStudyClientException) result).getStatusCode());
        assertTrue(result.getMessage().contains("Неверные данные"));
    }

    /**
     * Проверяет преобразование 5xx в NeoStudyServerException.
     */
    @Test
    void Given_serverErrorResponse_When_mapToNeoStudyException_Then_returnsServerException() throws Exception {
        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                503,
                "Service Unavailable",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        );

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertInstanceOf(NeoStudyServerException.class, result);
        assertEquals(503, ((NeoStudyServerException) result).getStatusCode());
    }

    /**
     * Проверяет преобразование таймаута в NeoStudyTimeoutException.
     */
    @Test
    void Given_timeoutException_When_mapToNeoStudyException_Then_returnsTimeoutException() throws Exception {
        // Arrange
        TimeoutException exception = new TimeoutException("timeout");

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertInstanceOf(NeoStudyTimeoutException.class, result);
        assertTrue(result.getMessage().contains("Таймаут запроса к NeoStudy"));
    }

    /**
     * Проверяет возврат исходного NeoStudyException без обёртки.
     */
    @Test
    void Given_existingNeoStudyException_When_mapToNeoStudyException_Then_returnsSameException() throws Exception {
        // Arrange
        NeoStudyException exception = new NeoStudyException("custom");

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertSame(exception, result);
    }

    /**
     * Проверяет оборачивание неожиданных ошибок в NeoStudyException.
     */
    @Test
    void Given_unexpectedException_When_mapToNeoStudyException_Then_returnsNeoStudyException() throws Exception {
        // Arrange
        IllegalStateException exception = new IllegalStateException("boom");

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertInstanceOf(NeoStudyException.class, result);
        assertTrue(result.getMessage().contains("Неожиданная ошибка при вызове NeoStudy API"));
    }

    /**
     * Проверяет использование сообщения исключения при некорректном JSON.
     */
    @Test
    void Given_invalidJsonErrorBody_When_mapToNeoStudyException_Then_usesExceptionMessage() throws Exception {
        // Arrange
        WebClientResponseException exception = WebClientResponseException.create(
                400,
                "Bad Request",
                HttpHeaders.EMPTY,
                "{invalid".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        // Act
        Throwable result = invokeMapToNeoStudyException(exception);

        // Assert
        assertInstanceOf(NeoStudyClientException.class, result);
        assertTrue(result.getMessage().contains("Bad Request"));
    }

    private Throwable invokeMapToNeoStudyException(Throwable throwable) throws Exception {
        Method method = NeoStudyClient.class.getDeclaredMethod("mapToNeoStudyException", Throwable.class);
        method.setAccessible(true);
        return (Throwable) method.invoke(client, throwable);
    }
}
