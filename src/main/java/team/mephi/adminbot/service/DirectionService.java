package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleDirection;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления направлениями.
 */
public interface DirectionService {
    /**
     * Получает список всех направлений.
     *
     * @return список всех направлений в виде DTO.
     */
    List<SimpleDirection> getAllDirections();

    /**
     * Получает список направлений с пагинацией и возможностью поиска по имени.
     *
     * @param pageable объект Pageable для пагинации результатов.
     * @param query    строка запроса для поиска по имени направления.
     * @return список направлений, соответствующих критериям поиска и пагинации.
     */
    List<SimpleDirection> getAllDirections(Pageable pageable, String query);

    /**
     * Подсчитывает общее количество направлений, соответствующих заданному запросу по имени.
     *
     * @param query строка запроса для поиска по имени направления.
     * @return количество направлений, соответствующих критериям поиска.
     */
    Integer countAllDirections(String query);

    /**
     * Получает направление по его id.
     *
     * @param id направления.
     * @return опциональный объект SimpleDirection, если направление с заданным именем существует.
     */
    Optional<SimpleDirection> getById(Long id);

    /**
     * Получает направление по его имени.
     *
     * @param name имя направления.
     * @return опциональный объект SimpleDirection, если направление с заданным именем существует.
     */
    Optional<SimpleDirection> getByName(String name);
}
