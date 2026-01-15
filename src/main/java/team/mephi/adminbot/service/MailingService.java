package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления рассылками.
 */
public interface MailingService {
    /**
     * Сохраняет информацию о рассылке.
     *
     * @param mailing объект SimpleMailing для сохранения.
     * @return сохраненный объект SimpleMailing.
     */
    SimpleMailing save(SimpleMailing mailing);

    /**
     * Находит рассылки по id.
     *
     * @param id       рассылки.
     * @return поток рассылок, соответствующих критериям поиска и пагинации.
     */
    Optional<SimpleMailing> findById(Long id);

    /**
     * Находит рассылки по имени с пагинацией.
     *
     * @param name     имя рассылки.
     * @param statuses список статусов рассылки.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток рассылок, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleMailing> findMailingByName(String name, List<String> statuses, Pageable pageable);

    /**
     * Подсчитывает количество рассылок по имени.
     *
     * @param name     имя рассылки.
     * @param statuses список статусов рассылки.
     * @return количество рассылок, соответствующих критериям поиска.
     */
    Integer countByName(String name, List<MailingStatus> statuses);

    /**
     * Удаляет все рассылки по их идентификаторам.
     *
     * @param ids коллекция идентификаторов рассылок для удаления.
     */
    void deleteAllById(Iterable<Long> ids);
}
