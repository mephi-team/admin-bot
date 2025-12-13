package team.mephi.adminbot.integration.neostudy.exception;

/**
 * Исключение для случаев, когда запрос к NeoStudy не успел выполниться.
 *
 * Используется, если произошёл таймаут:
 * - при установке соединения
 * - при ожидании ответа
 * - при отправке данных
 */
public class NeoStudyTimeoutException extends NeoStudyException {

    /**
     * Создаёт исключение с сообщением о таймауте.
     */
    public NeoStudyTimeoutException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением о таймауте
     * и исходной причиной ошибки.
     */
    public NeoStudyTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
