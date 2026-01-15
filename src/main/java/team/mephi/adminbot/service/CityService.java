package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CityDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления городами.
 */
public interface CityService {
    /**
     * Получает список всех городов.
     *
     * @return список всех городов в виде DTO.
     */
    List<CityDto> getAllCities();

    /**
     * Получает список городов с пагинацией и возможностью поиска по имени.
     *
     * @param pageable объект Pageable для пагинации результатов.
     * @param query    строка запроса для поиска по имени города.
     * @return список городов, соответствующих критериям поиска и пагинации.
     */
    List<CityDto> getAllCities(Pageable pageable, String query);

    /**
     * Получает город по его id.
     *
     * @param id города.
     * @return опциональный объект CityDto, если город с заданным именем существует.
     */
    Optional<CityDto> getById(String id);

    /**
     * Получает город по его имени.
     *
     * @param name имя города.
     * @return опциональный объект CityDto, если город с заданным именем существует.
     */
    Optional<CityDto> getByName(String name);
}
