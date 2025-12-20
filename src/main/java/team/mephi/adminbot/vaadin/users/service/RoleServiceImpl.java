package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.vaadin.users.components.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> getAllRoles(Pageable pageable, String query) {
        return roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getCode(), r.getName(), r.getDescription()))
                .toList();
    }

    @Override
    public Optional<RoleDto> getByCode(String code) {
        return roleRepository.findByCode(code).map(role -> new RoleDto(role.getCode(), role.getName(), role.getDescription()));
    }
}
