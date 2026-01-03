package team.mephi.adminbot.vaadin.users.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CityDto;

import java.util.List;
import java.util.Optional;

public interface CityService {
    List<CityDto> getAllCities(Pageable pageable, String query);
    Optional<CityDto> getById(String id);
}
