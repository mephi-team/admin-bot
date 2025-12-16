package team.mephi.adminbot.integration.neostudy.exception;

/**
 * Базовое исключение для ошибок при работе с NeoStudy.
 * <p>
 * Используется как общий родитель для всех ошибок,
 * связанных с интеграцией с NeoStudy
 * (HTTP-ошибки, ошибки ответа, проблемы соединения и т.д.).
 */
public class NeoStudyException extends RuntimeException {

    // HTTP-статус, который вернул NeoStudy (если есть)
    private final int statusCode;

    /**
     * Создаёт исключение только с сообщением об ошибке.
     * HTTP-статус при этом неизвестен.
     */
    public NeoStudyException(String message) {
        super(message);
        this.statusCode = 0;
    }

    /**
     * Создаёт исключение с сообщением и исходной причиной ошибки.
     * HTTP-статус при этом неизвестен.
     */
    public NeoStudyException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    /**
     * Создаёт исключение с сообщением и HTTP-статусом,
     * который вернул NeoStudy.
     */
    public NeoStudyException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Создаёт исключение с сообщением, HTTP-статусом
     * и исходной причиной ошибки.
     */
    public NeoStudyException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * Возвращает HTTP-статус ответа NeoStudy,
     * если он был известен на момент ошибки.
     */
    public int getStatusCode() {
        return statusCode;
    }
}

