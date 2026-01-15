package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CohortDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления когорты.
 */
public interface CohortService {
    /**
     * Получает список всех когорт.
     *
     * @return список всех когорт в виде DTO.
     */
    List<CohortDto> getAllCohorts();

    /**
     * Получает список когорт с пагинацией и возможностью поиска по имени.
     *
     * @param pageable объект Pageable для пагинации результатов.
     * @param query    строка запроса для поиска по имени когорты.
     * @return список когорт, соответствующих критериям поиска и пагинации.
     */
    List<CohortDto> getAllCohorts(Pageable pageable, String query);

    /**
     * Получает когорту по ее id.
     *
     * @param id когорты.
     * @return опциональный объект CohortDto, если когорта с заданным именем существует.
     */
    Optional<CohortDto> getById(String id);

    /**
     * Получает когорту по ее имени.
     *
     * @param name имя когорты.
     * @return опциональный объект CohortDto, если когорта с заданным именем существует.
     */
    Optional<CohortDto> getByName(String name);

    /**
     * Получает когорту по умолчанию.
     *
     * @return объект CohortDto, представляющий когорту по умолчанию.
     */
    CohortDto getDefaultCohort();
}
