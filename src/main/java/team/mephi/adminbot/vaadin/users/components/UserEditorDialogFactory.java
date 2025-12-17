package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.RoleDto;

import java.util.List;

@SpringComponent
public class UserEditorDialogFactory {

    private final RoleService roleService;
    private final List<RoleDto> allRoles;

    public UserEditorDialogFactory(RoleService roleService) {
        this.roleService = roleService;
        this.allRoles = roleService.getAllRoles();
    }

    public UserEditorDialog create() {
        return new UserEditorDialog(allRoles);
    }
}
