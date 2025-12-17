package team.mephi.adminbot.vaadin.users.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.Map;

@SpringComponent
public class UsersPresenterFactory {

    private final Map<String, UserDataProvider> providers;

    public UsersPresenterFactory(Map<String, UserDataProvider> providers) {
        this.providers = providers;
    }

    public UserDataProvider createDataProvider(String role) {
        return providers.get(role);
    }
}
