package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.vaadin.users.components.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final List<RoleDto> roles = new ArrayList<>(List.of(RoleDto.builder().code(null).name("Все").build()));

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        init();
    }

    void init() {
        this.roles.addAll(roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getCode(), r.getName()))
                .toList());
    }

    @Override
    public List<RoleDto> getAllRoles() {
        if (roles.size() < 2) init();
        return roles;
    }

    @Override
    public List<RoleDto> getAllRoles(Pageable pageable, String query) {
        if (roles.size() < 2) init();
        return roles;
    }

    @Override
    public Optional<RoleDto> getByCode(String code) {
        if (Objects.isNull(code)) return Optional.empty();
        if (roles.size() < 2) init();
        return roles.stream().filter(r -> code.equals(r.getCode())).findAny();
    }

    @Override
    public Optional<RoleDto> getByName(String name) {
        if (Objects.isNull(name)) return Optional.empty();
        if (roles.size() < 2) init();
        return roles.stream().filter(r -> r.getName().equals(name)).findAny();
    }
}
