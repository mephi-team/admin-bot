package team.mephi.adminbot.vaadin.users.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> getAllRoles(Pageable pageable, String query);
    Optional<RoleDto> getByCode(String code);
}
