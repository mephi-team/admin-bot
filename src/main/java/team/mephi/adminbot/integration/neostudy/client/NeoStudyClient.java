package team.mephi.adminbot.integration.neostudy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import team.mephi.adminbot.integration.neostudy.config.NeoStudyProperties;
import team.mephi.adminbot.integration.neostudy.dto.*;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyClientException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyServerException;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyTimeoutException;

import java.time.Duration;
import java.util.List;

/**
 * Клиент для работы с API NeoStudy.
 *
 * Этот класс инкапсулирует все HTTP-запросы к NeoStudy:
 * - работа с пользователями (создание, обновление, получение)
 * - работа с курсами
 * - работа с записями на курсы
 *
 * Внутри уже есть:
 * - централизованная обработка ошибок
 * - логирование запросов и ответов
 * - retry для временных сбоев
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NeoStudyClient {

    // WebClient с заранее настроенными таймаутами и авторизацией
    @Qualifier("neostudyWebClient")
    private final WebClient webClient;

    // Настройки интеграции (retry, таймауты и т.д.)
    private final NeoStudyProperties properties;

    // ObjectMapper для разбора ошибок NeoStudy
    private final ObjectMapper objectMapper;

    // Пути API NeoStudy
    private static final String USERS_ENDPOINT = "/api/v1/users";
    private static final String COURSES_ENDPOINT = "/api/v1/courses";
    private static final String ENROLLMENTS_ENDPOINT = "/api/v1/enrollments";

    /**
     * Создаёт пользователя в NeoStudy.
     */
    public Mono<NeoStudyUserResponse> createUser(NeoStudyUserRequest request) {
        log.info(
                "Создание пользователя в NeoStudy: externalId={}, name={}",
                request.getExternalId(),
                request.getName()
        );

        return webClient.post()
                .uri(USERS_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NeoStudyUserResponse.class)
                .doOnSuccess(response -> log.info(
                        "Пользователь создан в NeoStudy: id={}, externalId={}",
                        response.getId(),
                        response.getExternalId()
                ))
                .doOnError(error -> log.error(
                        "Ошибка создания пользователя в NeoStudy: externalId={}",
                        request.getExternalId(),
                        error
                ))
                .retryWhen(createRetrySpec("createUser"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Обновляет данные пользователя в NeoStudy.
     */
    public Mono<NeoStudyUserResponse> updateUser(String userId, NeoStudyUserRequest request) {
        log.info(
                "Обновление пользователя в NeoStudy: userId={}, externalId={}",
                userId,
                request.getExternalId()
        );

        return webClient.put()
                .uri(USERS_ENDPOINT + "/{userId}", userId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NeoStudyUserResponse.class)
                .doOnSuccess(response -> log.info(
                        "Пользователь обновлён в NeoStudy: id={}",
                        response.getId()
                ))
                .doOnError(error -> log.error(
                        "Ошибка обновления пользователя в NeoStudy: userId={}",
                        userId,
                        error
                ))
                .retryWhen(createRetrySpec("updateUser"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Получает пользователя из NeoStudy по его ID.
     */
    public Mono<NeoStudyUserResponse> getUser(String userId) {
        log.debug("Получение пользователя из NeoStudy: userId={}", userId);

        return webClient.get()
                .uri(USERS_ENDPOINT + "/{userId}", userId)
                .retrieve()
                .bodyToMono(NeoStudyUserResponse.class)
                .doOnError(error -> log.error(
                        "Ошибка получения пользователя из NeoStudy: userId={}",
                        userId,
                        error
                ))
                .retryWhen(createRetrySpec("getUser"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Получает пользователя из NeoStudy по externalId
     * (ID пользователя в нашей системе).
     */
    public Mono<NeoStudyUserResponse> getUserByExternalId(String externalId) {
        log.debug(
                "Получение пользователя из NeoStudy по externalId={}",
                externalId
        );

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USERS_ENDPOINT)
                        .queryParam("external_id", externalId)
                        .build()
                )
                .retrieve()
                .bodyToMono(NeoStudyUserResponse.class)
                .doOnError(error -> log.error(
                        "Ошибка получения пользователя из NeoStudy: externalId={}",
                        externalId,
                        error
                ))
                .retryWhen(createRetrySpec("getUserByExternalId"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Загружает список всех курсов (направлений) из NeoStudy.
     */
    public Mono<List<NeoStudyCourseResponse>> getCourses() {
        log.info("Загрузка курсов из NeoStudy");

        return webClient.get()
                .uri(COURSES_ENDPOINT)
                .retrieve()
                .bodyToFlux(NeoStudyCourseResponse.class)
                .collectList()
                .doOnSuccess(courses ->
                        log.info("Получено курсов из NeoStudy: {}", courses.size())
                )
                .doOnError(error ->
                        log.error("Ошибка загрузки курсов из NeoStudy", error)
                )
                .retryWhen(createRetrySpec("getCourses"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Получает курс из NeoStudy по ID.
     */
    public Mono<NeoStudyCourseResponse> getCourse(String courseId) {
        log.debug("Получение курса из NeoStudy: courseId={}", courseId);

        return webClient.get()
                .uri(COURSES_ENDPOINT + "/{courseId}", courseId)
                .retrieve()
                .bodyToMono(NeoStudyCourseResponse.class)
                .doOnError(error -> log.error(
                        "Ошибка получения курса из NeoStudy: courseId={}",
                        courseId,
                        error
                ))
                .retryWhen(createRetrySpec("getCourse"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Создаёт запись пользователя на курс в NeoStudy.
     */
    public Mono<NeoStudyEnrollmentResponse> createEnrollment(
            NeoStudyEnrollmentRequest request) {

        log.info(
                "Создание записи в NeoStudy: userId={}, courseId={}",
                request.getUserId(),
                request.getCourseId()
        );

        return webClient.post()
                .uri(ENROLLMENTS_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NeoStudyEnrollmentResponse.class)
                .doOnSuccess(response -> log.info(
                        "Запись создана в NeoStudy: id={}, userId={}, courseId={}",
                        response.getId(),
                        response.getUserId(),
                        response.getCourseId()
                ))
                .doOnError(error -> log.error(
                        "Ошибка создания записи в NeoStudy: userId={}, courseId={}",
                        request.getUserId(),
                        request.getCourseId(),
                        error
                ))
                .retryWhen(createRetrySpec("createEnrollment"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Обновляет статус записи на курс в NeoStudy.
     */
    public Mono<NeoStudyEnrollmentResponse> updateEnrollment(
            String enrollmentId,
            NeoStudyEnrollmentRequest request) {

        log.info(
                "Обновление записи в NeoStudy: enrollmentId={}, status={}",
                enrollmentId,
                request.getStatus()
        );

        return webClient.put()
                .uri(ENROLLMENTS_ENDPOINT + "/{enrollmentId}", enrollmentId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NeoStudyEnrollmentResponse.class)
                .doOnSuccess(response -> log.info(
                        "Запись обновлена в NeoStudy: id={}",
                        response.getId()
                ))
                .doOnError(error -> log.error(
                        "Ошибка обновления записи в NeoStudy: enrollmentId={}",
                        enrollmentId,
                        error
                ))
                .retryWhen(createRetrySpec("updateEnrollment"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Получает запись на курс из NeoStudy по ID.
     */
    public Mono<NeoStudyEnrollmentResponse> getEnrollment(String enrollmentId) {
        log.debug(
                "Получение записи из NeoStudy: enrollmentId={}",
                enrollmentId
        );

        return webClient.get()
                .uri(ENROLLMENTS_ENDPOINT + "/{enrollmentId}", enrollmentId)
                .retrieve()
                .bodyToMono(NeoStudyEnrollmentResponse.class)
                .doOnError(error -> log.error(
                        "Ошибка получения записи из NeoStudy: enrollmentId={}",
                        enrollmentId,
                        error
                ))
                .retryWhen(createRetrySpec("getEnrollment"))
                .onErrorMap(this::mapToNeoStudyException);
    }

    /**
     * Удобный метод для обновления статуса записи —
     * просто повторно запрашивает данные из NeoStudy.
     */
    public Mono<NeoStudyEnrollmentResponse> syncEnrollmentStatus(String enrollmentId) {
        log.info(
                "Синхронизация статуса записи из NeoStudy: enrollmentId={}",
                enrollmentId
        );
        return getEnrollment(enrollmentId);
    }

    /**
     * Создаёт правила retry для реактивных запросов.
     *
     * Повторяем запросы:
     * - при ошибках 5xx
     * - при таймаутах
     */
    private Retry createRetrySpec(String operationName) {
        return Retry.backoff(
                        properties.getRetry().getMaxAttempts(),
                        properties.getRetry().getBackoffDelay()
                )
                .multiplier(properties.getRetry().getMultiplier())
                .maxBackoff(Duration.ofSeconds(30))
                .doBeforeRetry(retrySignal -> log.warn(
                        "Повтор операции {} (попытка {}/{})",
                        operationName,
                        retrySignal.totalRetries() + 1,
                        properties.getRetry().getMaxAttempts(),
                        retrySignal.failure()
                ))
                .filter(throwable -> {
                    // Повторяем только при серверных ошибках и таймаутах
                    if (throwable instanceof WebClientResponseException ex) {
                        return ex.getStatusCode().is5xxServerError();
                    }
                    return throwable instanceof java.util.concurrent.TimeoutException
                            || throwable instanceof java.net.SocketTimeoutException;
                });
    }

    /**
     * Преобразует любые ошибки в специализированные NeoStudy-исключения.
     */
    private Throwable mapToNeoStudyException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            int statusCode = ex.getStatusCode().value();
            String message = extractErrorMessage(ex);

            if (statusCode >= 400 && statusCode < 500) {
                return new NeoStudyClientException(
                        "Ошибка клиента NeoStudy: " + message,
                        statusCode,
                        ex
                );
            } else if (statusCode >= 500) {
                return new NeoStudyServerException(
                        "Ошибка сервера NeoStudy: " + message,
                        statusCode,
                        ex
                );
            }
        }

        if (throwable instanceof java.util.concurrent.TimeoutException
                || throwable instanceof java.net.SocketTimeoutException) {
            return new NeoStudyTimeoutException(
                    "Таймаут запроса к NeoStudy",
                    throwable
            );
        }

        if (throwable instanceof NeoStudyException) {
            return throwable;
        }

        return new NeoStudyException(
                "Неожиданная ошибка при вызове NeoStudy API: " + throwable.getMessage(),
                throwable
        );
    }

    /**
     * Пытается достать понятное сообщение об ошибке из ответа NeoStudy.
     *
     * Если NeoStudy вернул JSON с описанием ошибки —
     * пробуем его разобрать.
     */
    private String extractErrorMessage(WebClientResponseException ex) {
        try {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null && !responseBody.isEmpty()) {
                NeoStudyErrorResponse errorResponse =
                        objectMapper.readValue(
                                responseBody,
                                NeoStudyErrorResponse.class
                        );
                if (errorResponse.getMessage() != null) {
                    return errorResponse.getMessage();
                }
            }
        } catch (JsonProcessingException e) {
            log.debug("Не удалось разобрать ошибку от NeoStudy", e);
        }
        return ex.getMessage();
    }
}
