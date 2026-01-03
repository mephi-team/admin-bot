package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class StudentDataProvider extends BaseUserDataProvider {

    public StudentDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "student";
    }
}