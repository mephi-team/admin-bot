package team.mephi.adminbot.integration.neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import team.mephi.adminbot.integration.neostudy.client.NeoStudyClient;
import team.mephi.adminbot.integration.neostudy.config.NeoStudyProperties;
import team.mephi.adminbot.integration.neostudy.dto.*;
import team.mephi.adminbot.integration.neostudy.exception.NeoStudyException;
import team.mephi.adminbot.integration.neostudy.mapper.NeoStudyMapper;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.DirectionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с интеграцией NeoStudy.
 *
 * Здесь сосредоточена вся бизнес-логика:
 * - регистрация и синхронизация пользователей
 * - синхронизация курсов (направлений)
 * - запись пользователей на курсы
 * - обработка вебхуков
 *
 * Во всех методах используются:
 * - транзакции
 * - retry при временных ошибках
 * - логирование
 * - единая обработка ошибок
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NeoStudyService {

    private final NeoStudyClient neoStudyClient;
    private final NeoStudyMapper neoStudyMapper;
    private final UserRepository userRepository;
    private final DirectionRepository directionRepository;
    private final NeoStudyProperties properties;

    /**
     * Регистрирует пользователя в NeoStudy.
     *
     * Логика такая:
     * - если пользователь уже есть в NeoStudy (по externalId) — обновляем его
     * - если нет — создаём нового
     *
     * После успешной операции сохраняем:
     * - neostudyExternalId
     * - время синхронизации
     */
    @Transactional
    @Retryable(
            retryFor = {NeoStudyException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public User registerUser(User user) {
        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Регистрация пользователя пропущена.");
            return user;
        }

        log.info(
                "Регистрация пользователя в NeoStudy: userId={}, externalId={}",
                user.getId(),
                user.getExternalId()
        );

        try {
            NeoStudyUserRequest request =
                    neoStudyMapper.toNeoStudyUserRequest(user);

            // Сначала пробуем найти пользователя в NeoStudy
            Mono<NeoStudyUserResponse> responseMono = neoStudyClient
                    .getUserByExternalId(user.getExternalId())
                    .onErrorResume(error -> {
                        // Если пользователя нет — создаём нового
                        log.debug(
                                "Пользователь не найден в NeoStudy, создаём нового: externalId={}",
                                user.getExternalId()
                        );
                        return neoStudyClient.createUser(request);
                    })
                    .flatMap(existingUser -> {
                        // Если пользователь есть — обновляем его
                        log.debug(
                                "Пользователь найден в NeoStudy, обновляем: neostudyId={}",
                                existingUser.getId()
                        );
                        return neoStudyClient.updateUser(existingUser.getId(), request);
                    });

            NeoStudyUserResponse response = responseMono.block();

            if (response != null) {
                // Сохраняем NeoStudy ID и время синхронизации
                user.setNeostudyExternalId(response.getId());
                user.setNeostudySyncedAt(LocalDateTime.now());

                User savedUser = userRepository.save(user);

                log.info(
                        "Пользователь успешно зарегистрирован в NeoStudy: userId={}, neostudyId={}",
                        savedUser.getId(),
                        savedUser.getNeostudyExternalId()
                );

                return savedUser;
            }

            return user;
        } catch (Exception e) {
            log.error(
                    "Ошибка регистрации пользователя в NeoStudy: userId={}",
                    user.getId(),
                    e
            );
            throw new NeoStudyException(
                    "Не удалось зарегистрировать пользователя в NeoStudy",
                    e
            );
        }
    }

    /**
     * Синхронизирует данные пользователя с NeoStudy.
     *
     * Если пользователь ещё не зарегистрирован в NeoStudy —
     * сначала выполняется регистрация.
     */
    @Transactional
    @Retryable(
            retryFor = {NeoStudyException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public User syncUser(User user) {
        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Синхронизация пользователя пропущена.");
            return user;
        }

        // Если нет NeoStudy ID — сначала регистрируем пользователя
        if (user.getNeostudyExternalId() == null) {
            log.warn(
                    "У пользователя нет NeoStudy ID, выполняем регистрацию: userId={}",
                    user.getId()
            );
            return registerUser(user);
        }

        log.info(
                "Синхронизация пользователя с NeoStudy: userId={}, neostudyId={}",
                user.getId(),
                user.getNeostudyExternalId()
        );

        try {
            NeoStudyUserRequest request =
                    neoStudyMapper.toNeoStudyUserRequest(user);

            NeoStudyUserResponse response = neoStudyClient
                    .updateUser(user.getNeostudyExternalId(), request)
                    .block();

            if (response != null) {
                neoStudyMapper.updateUserFromNeoStudy(user, response);
                user.setNeostudySyncedAt(LocalDateTime.now());
                user = userRepository.save(user);

                log.info(
                        "Пользователь успешно синхронизирован с NeoStudy: userId={}",
                        user.getId()
                );
            }

            return user;
        } catch (Exception e) {
            log.error(
                    "Ошибка синхронизации пользователя с NeoStudy: userId={}",
                    user.getId(),
                    e
            );
            throw new NeoStudyException(
                    "Не удалось синхронизировать пользователя с NeoStudy",
                    e
            );
        }
    }

    /**
     * Загружает курсы из NeoStudy и синхронизирует их с нашей базой.
     *
     * Для каждого курса:
     * - ищем направление по коду
     * - если найдено — обновляем
     * - если нет — создаём новое
     */
    @Transactional
    @Retryable(
            retryFor = {NeoStudyException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public List<Direction> syncCourses() {
        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Синхронизация курсов пропущена.");
            return List.of();
        }

        log.info("Синхронизация курсов из NeoStudy");

        try {
            List<NeoStudyCourseResponse> courses =
                    neoStudyClient.getCourses().block();

            if (courses == null || courses.isEmpty()) {
                log.warn("В NeoStudy не найдено ни одного курса");
                return List.of();
            }

            List<Direction> directions = courses.stream()
                    .map(course -> {
                        // Ищем направление по коду
                        Optional<Direction> existingDirection =
                                directionRepository.findAll().stream()
                                        .filter(d -> d.getCode().equals(course.getCode()))
                                        .findFirst();

                        Direction direction;
                        if (existingDirection.isPresent()) {
                            direction = existingDirection.get();
                            neoStudyMapper.updateDirectionFromNeoStudy(direction, course);
                        } else {
                            direction = neoStudyMapper.toDirection(course);
                        }

                        // Обновляем NeoStudy ID и время синхронизации
                        direction.setNeostudyExternalId(course.getId());
                        direction.setNeostudySyncedAt(LocalDateTime.now());

                        return directionRepository.save(direction);
                    })
                    .toList();

            log.info("Синхронизировано курсов: {}", directions.size());
            return directions;
        } catch (Exception e) {
            log.error("Ошибка синхронизации курсов из NeoStudy", e);
            throw new NeoStudyException(
                    "Не удалось синхронизировать курсы из NeoStudy",
                    e
            );
        }
    }

    /**
     * Создаёт запись пользователя на курс в NeoStudy.
     *
     * Перед созданием гарантирует, что:
     * - пользователь зарегистрирован в NeoStudy
     * - курс синхронизирован с NeoStudy
     */
    @Transactional
    @Retryable(
            retryFor = {NeoStudyException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public NeoStudyEnrollmentResponse createEnrollment(
            User user,
            Direction direction,
            String status) {

        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Создание записи пропущено.");
            return null;
        }

        // Проверяем регистрацию пользователя
        User registeredUser = user;
        if (user.getNeostudyExternalId() == null) {
            log.info(
                    "Пользователь не зарегистрирован в NeoStudy, регистрируем: userId={}",
                    user.getId()
            );
            registeredUser = registerUser(user);
        }

        // Проверяем синхронизацию курса
        Direction syncedDirection = direction;
        if (direction.getNeostudyExternalId() == null) {
            log.info(
                    "Курс не синхронизирован с NeoStudy, синхронизируем: directionId={}",
                    direction.getId()
            );
            syncCourses();
            syncedDirection = directionRepository.findById(direction.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Направление не найдено: " + direction.getId()
                            )
                    );
        }

        log.info(
                "Создание записи в NeoStudy: userId={}, directionId={}, status={}",
                registeredUser.getId(),
                syncedDirection.getId(),
                status
        );

        try {
            NeoStudyEnrollmentRequest request =
                    neoStudyMapper.toNeoStudyEnrollmentRequest(
                            registeredUser.getNeostudyExternalId(),
                            syncedDirection.getNeostudyExternalId(),
                            status
                    );

            NeoStudyEnrollmentResponse response =
                    neoStudyClient.createEnrollment(request).block();

            if (response != null) {
                log.info(
                        "Запись успешно создана в NeoStudy: enrollmentId={}",
                        response.getId()
                );
            }

            return response;
        } catch (Exception e) {
            log.error(
                    "Ошибка создания записи в NeoStudy: userId={}, directionId={}",
                    registeredUser.getId(),
                    syncedDirection.getId(),
                    e
            );
            throw new NeoStudyException(
                    "Не удалось создать запись в NeoStudy",
                    e
            );
        }
    }

    /**
     * Обновляет статус записи на курс по данным из NeoStudy.
     */
    @Retryable(
            retryFor = {NeoStudyException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public NeoStudyEnrollmentResponse syncEnrollmentStatus(String enrollmentId) {
        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Синхронизация записи пропущена.");
            return null;
        }

        log.info(
                "Синхронизация статуса записи из NeoStudy: enrollmentId={}",
                enrollmentId
        );

        try {
            NeoStudyEnrollmentResponse response =
                    neoStudyClient.syncEnrollmentStatus(enrollmentId).block();

            if (response != null) {
                log.info(
                        "Статус записи обновлён: enrollmentId={}, status={}",
                        enrollmentId,
                        response.getStatus()
                );
            }

            return response;
        } catch (Exception e) {
            log.error(
                    "Ошибка синхронизации статуса записи: enrollmentId={}",
                    enrollmentId,
                    e
            );
            throw new NeoStudyException(
                    "Не удалось синхронизировать статус записи",
                    e
            );
        }
    }

    /**
     * Обрабатывает вебхуки от NeoStudy.
     *
     * В зависимости от типа события
     * вызывает нужный обработчик.
     */
    @Transactional
    public void processWebhook(NeoStudyWebhookPayload payload) {
        if (!properties.isEnabled()) {
            log.warn("Интеграция NeoStudy выключена. Вебхук проигнорирован.");
            return;
        }

        log.info(
                "Обработка вебхука от NeoStudy: eventType={}",
                payload.getEventType()
        );

        try {
            String eventType = payload.getEventType();

            if (eventType == null) {
                log.warn("В вебхуке отсутствует eventType");
                return;
            }

            switch (eventType) {
                case "enrollment.updated":
                case "enrollment.status_changed":
                    handleEnrollmentUpdate(payload);
                    break;
                case "user.updated":
                    handleUserUpdate(payload);
                    break;
                case "course.updated":
                    handleCourseUpdate(payload);
                    break;
                default:
                    log.debug("Необработанный тип события вебхука: {}", eventType);
            }
        } catch (Exception e) {
            log.error(
                    "Ошибка обработки вебхука от NeoStudy: eventType={}",
                    payload.getEventType(),
                    e
            );
            throw new NeoStudyException("Ошибка обработки вебхука NeoStudy", e);
        }
    }

    /**
     * Обработка вебхука об изменении записи на курс.
     * Пока заглушка — реализуется по бизнес-логике.
     */
    private void handleEnrollmentUpdate(NeoStudyWebhookPayload payload) {
        log.debug("Обработка вебхука обновления записи");
    }

    /**
     * Обработка вебхука об обновлении пользователя.
     * Пока заглушка — реализуется по бизнес-логике.
     */
    private void handleUserUpdate(NeoStudyWebhookPayload payload) {
        log.debug("Обработка вебхука обновления пользователя");
    }

    /**
     * Обработка вебхука об обновлении курса.
     * Пока заглушка — реализуется по бизнес-логике.
     */
    private void handleCourseUpdate(NeoStudyWebhookPayload payload) {
        log.debug("Обработка вебхука обновления курса");
    }
}
