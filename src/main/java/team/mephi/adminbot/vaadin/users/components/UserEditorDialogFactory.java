package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class UserEditorDialogFactory {

    private final RoleService roleService;

    public UserEditorDialogFactory(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserEditorDialog create() {
        return new UserEditorDialog(roleService);
    }
}
