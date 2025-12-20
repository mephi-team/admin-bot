package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.vaadin.users.components.CityService;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    @Override
    public List<CityDto> getAllCities(Pageable pageable, String query) {
        return List.of(new CityDto(1L, "Москва"));
    }

    @Override
    public Optional<CityDto> getById(Long id) {
        return Optional.empty();
    }
}
