package team.mephi.adminbot.integration.neostudy.exception;

/**
 * Исключение для серверных ошибок NeoStudy.
 *
 * Используется, когда NeoStudy возвращает HTTP-ошибки 5xx,
 * то есть проблема возникла на стороне самого сервиса.
 *
 * Примеры таких ошибок:
 * - 500 Internal Server Error
 * - 502 Bad Gateway
 * - 503 Service Unavailable
 */
public class NeoStudyServerException extends NeoStudyException {

    /**
     * Создаёт исключение с сообщением об ошибке
     * и HTTP-статусом ответа NeoStudy.
     */
    public NeoStudyServerException(String message, int statusCode) {
        super(message, statusCode);
    }

    /**
     * Создаёт исключение с сообщением, HTTP-статусом
     * и исходной причиной ошибки.
     */
    public NeoStudyServerException(String message, int statusCode, Throwable cause) {
        super(message, statusCode, cause);
    }
}