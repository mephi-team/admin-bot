package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления шаблонами.
 */
public interface TemplateService {
    /**
     * Получает все шаблоны.
     *
     * @return Список всех шаблонов.
     */
    List<SimpleTemplate> findAll();

    /**
     * Получает все шаблоны с пагинацией и возможностью поиска по строке.
     *
     * @param pageable Объект Pageable для пагинации результатов.
     * @param query    Строка запроса для фильтрации шаблонов.
     * @return Список шаблонов, соответствующих критериям поиска и пагинации.
     */
    List<SimpleTemplate> findAll(Pageable pageable, String query);

    /**
     * Сохраняет шаблон.
     *
     * @param template объект SimpleTemplate для сохранения.
     * @return сохраненный объект SimpleTemplate.
     */
    SimpleTemplate save(SimpleTemplate template);

    /**
     * Находит шаблон по его идентификатору.
     *
     * @param id Идентификатор шаблона.
     * @return Опциональный шаблон, если он существует.
     */
    Optional<SimpleTemplate> findById(Long id);

    /**
     * Находит шаблоны по имени с пагинацией.
     *
     * @param name     имя шаблона.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток шаблонов, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleTemplate> findAllByName(String name, Pageable pageable);

    /**
     * Подсчитывает количество шаблонов по имени.
     *
     * @param name имя шаблона.
     * @return количество шаблонов, соответствующих критериям поиска.
     */
    Integer countByName(String name);

    /**
     * Удаляет все шаблоны по их идентификаторам.
     *
     * @param ids коллекция идентификаторов шаблонов для удаления.
     */
    void deleteAllById(Iterable<Long> ids);
}
