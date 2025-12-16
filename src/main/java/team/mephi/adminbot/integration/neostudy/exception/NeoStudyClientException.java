package team.mephi.adminbot.integration.neostudy.exception;

/**
 * Исключение для ошибок клиента при работе с API NeoStudy.
 * <p>
 * Используется, когда NeoStudy возвращает HTTP-ошибки 4xx,
 * то есть запрос был некорректным или у пользователя
 * нет прав на выполнение операции.
 * <p>
 * Примеры таких ошибок:
 * - 400 Bad Request
 * - 401 Unauthorized
 * - 403 Forbidden
 * - 404 Not Found
 */
public class NeoStudyClientException extends NeoStudyException {

    /**
     * Создаёт исключение с сообщением об ошибке
     * и HTTP-статусом ответа NeoStudy.
     */
    public NeoStudyClientException(String message, int statusCode) {
        super(message, statusCode);
    }

    /**
     * Создаёт исключение с сообщением, HTTP-статусом
     * и исходной причиной ошибки.
     */
    public NeoStudyClientException(String message, int statusCode, Throwable cause) {
        super(message, statusCode, cause);
    }
}
