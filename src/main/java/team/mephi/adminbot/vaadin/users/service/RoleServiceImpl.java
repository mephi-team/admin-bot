package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.vaadin.users.components.RoleService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private List<RoleDto> roles;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        init();
    }

    void init() {
        this.roles = roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getCode(), r.getName()))
                .toList();
    }

    @Override
    public List<RoleDto> getAllRoles() {
        if (Objects.isNull(roles) || roles.isEmpty()) init();
        return roles;
    }

    @Override
    public List<RoleDto> getAllRoles(Pageable pageable, String query) {
        if (Objects.isNull(roles) || roles.isEmpty()) init();
        return roles;
    }

    @Override
    public Optional<RoleDto> getByCode(String code) {
        if (Objects.isNull(code)) return Optional.empty();
        if (Objects.isNull(roles) || roles.isEmpty()) init();
        return roles.stream().filter(r -> r.getCode().equals(code)).findAny();
    }

    @Override
    public Optional<RoleDto> getByName(String name) {
        if (Objects.isNull(name)) return Optional.empty();
        if (Objects.isNull(roles) || roles.isEmpty()) init();
        return roles.stream().filter(r -> r.getName().equals(name)).findAny();
    }
}
