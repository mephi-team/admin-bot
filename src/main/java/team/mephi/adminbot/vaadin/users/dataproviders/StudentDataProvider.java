package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class StudentDataProvider extends BaseUserDataProvider {

    public StudentDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "student";
    }
}