package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.vaadin.users.components.CityService;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final List<CityDto> cities = List.of(
            new CityDto("9cfa1706-2ab2-4ebb-b9f4-76b08b71e26e", "Москва"),
            new CityDto("bd7ab214-fb32-4591-be65-80bc67b91401", "Омск")
    );

    @Override
    public List<CityDto> getAllCities(Pageable pageable, String query) {
        return cities;
    }

    @Override
    public Optional<CityDto> getById(String id) {
        return cities.stream().filter(c -> c.getId().equals(id)).findAny();
    }
}
