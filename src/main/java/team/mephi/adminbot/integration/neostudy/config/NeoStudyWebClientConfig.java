package team.mephi.adminbot.integration.neostudy.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Конфигурация WebClient для работы с API NeoStudy.
 *
 * Здесь создаётся и настраивается WebClient,
 * через который выполняются все запросы в NeoStudy.
 *
 * Что настраивается:
 * - таймауты подключения, чтения и записи
 * - пул соединений
 * - базовый URL
 * - стандартные HTTP-заголовки
 * - авторизация через API-ключ
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class NeoStudyWebClientConfig {

    // Настройки интеграции с NeoStudy (URL, таймауты, ключ и т.д.)
    private final NeoStudyProperties properties;

    /**
     * Создаёт WebClient для запросов в NeoStudy.
     *
     * WebClient настраивается с учётом:
     * - базового URL из конфигурации
     * - таймаутов
     * - пула соединений
     * - стандартных заголовков
     * - авторизации
     */
    @Bean(name = "neostudyWebClient")
    public WebClient neostudyWebClient() {

        // Создаём пул соединений для NeoStudy
        ConnectionProvider connectionProvider = ConnectionProvider.builder("neostudy")
                // Максимальное количество одновременных соединений
                .maxConnections(100)

                // Сколько ждать свободного соединения из пула
                .pendingAcquireTimeout(Duration.ofSeconds(10))

                // Как часто чистить неиспользуемые соединения
                .evictInBackground(Duration.ofSeconds(30))
                .build();

        // Настраиваем HTTP-клиент с таймаутами
        HttpClient httpClient = HttpClient.create(connectionProvider)

                // Таймаут на установку соединения
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) properties.getTimeout().getConnect().toMillis()
                )

                // Таймаут на получение ответа
                .responseTimeout(properties.getTimeout().getRead())

                // Таймауты на чтение и запись данных
                .doOnConnected(conn -> conn
                        .addHandlerLast(
                                new ReadTimeoutHandler(
                                        properties.getTimeout().getRead().toSeconds(),
                                        TimeUnit.SECONDS
                                )
                        )
                        .addHandlerLast(
                                new WriteTimeoutHandler(
                                        properties.getTimeout().getWrite().toSeconds(),
                                        TimeUnit.SECONDS
                                )
                        )
                );

        // Собираем WebClient с базовой конфигурацией
        WebClient.Builder builder = WebClient.builder()

                // Базовый URL API NeoStudy
                .baseUrl(properties.getBaseUrl())

                // Используем настроенный HTTP-клиент
                .clientConnector(new ReactorClientHttpConnector(httpClient))

                // По умолчанию работаем с JSON
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        // Добавляем заголовок авторизации, если задан API-ключ
        if (properties.getApiKey() != null && !properties.getApiKey().isEmpty()) {

            // Передаём API-ключ как Bearer-токен
            builder.defaultHeader(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + properties.getApiKey()
            );

            log.info("WebClient для NeoStudy настроен с API-ключом");
        } else {
            // Предупреждаем, если ключ не задан
            log.warn("API-ключ NeoStudy не задан. Запросы могут завершаться ошибкой.");
        }

        return builder.build();
    }
}
