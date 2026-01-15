package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CityDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления городами.
 */
public interface CityService {
    List<CityDto> getAllCities();

    List<CityDto> getAllCities(Pageable pageable, String query);

    Optional<CityDto> getById(String id);

    Optional<CityDto> getByName(String name);
}
