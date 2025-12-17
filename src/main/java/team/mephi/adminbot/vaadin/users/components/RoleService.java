package team.mephi.adminbot.vaadin.users.components;

import team.mephi.adminbot.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> getAllRoles();
    Optional<RoleDto> getByCode(String code);
}
