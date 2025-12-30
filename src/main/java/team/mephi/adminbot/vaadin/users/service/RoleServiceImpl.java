package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.vaadin.users.components.RoleService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private List<RoleDto> roles;
    private Map<String, RoleDto> roleByDto;

    public RoleServiceImpl(RoleRepository roleRepository) {
        System.out.println("!!! RoleServiceImpl");
        this.roleRepository = roleRepository;
        init();
    }

    void init() {
        this.roles = roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getCode(), r.getName(), r.getDescription()))
                .toList();
        this.roleByDto = roles.stream().collect(Collectors.toMap(RoleDto::getCode, item -> item));
    }

    @Override
    public List<RoleDto> getAllRoles(Pageable pageable, String query) {
        if (Objects.isNull(roles) || roles.isEmpty()) init();
        return roles;
    }

    @Override
    public Optional<RoleDto> getByCode(String code) {
        if (Objects.isNull(roleByDto) || roleByDto.isEmpty()) init();
        System.out.println("!!! roleByDto " + roleByDto + " , code " + code);
        return Optional.of(roleByDto.get(code));
    }
}
