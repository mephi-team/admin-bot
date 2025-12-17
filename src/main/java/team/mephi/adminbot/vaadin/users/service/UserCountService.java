package team.mephi.adminbot.vaadin.users.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Map;

@Service
public class UserCountService {
    private UserRepository userRepository;

    public UserCountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Long> getAllCounts() {
        return userRepository.countsByRole();
    }
}
