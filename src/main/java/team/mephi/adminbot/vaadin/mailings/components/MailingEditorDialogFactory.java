package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;

@SpringComponent
public class MailingEditorDialogFactory {
    private List<UserDto> allUsers;
    public MailingEditorDialogFactory(UserService userService) {
        this.allUsers = userService.getAllUsers();
        System.out.println("allUsers " + allUsers);
    }
    public MailingEditorDialog create() {
        return new MailingEditorDialog(allUsers);
    }
}
