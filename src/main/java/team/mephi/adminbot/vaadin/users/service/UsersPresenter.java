package team.mephi.adminbot.vaadin.users.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.Map;

@Service
public class UsersPresenter {

    private final Map<String, UserDataProvider> providers;

    public UsersPresenter(Map<String, UserDataProvider> providers) {
        this.providers = providers;
    }

    public UserDataProvider createDataProvider(String role) {
        return providers.get(role);
    }
}
