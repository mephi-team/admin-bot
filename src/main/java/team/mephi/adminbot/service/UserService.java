package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void approveCandidate(Long userId) {
        User user = getCandidate(userId);

        Role student = roleRepository.findByName("student")
                .orElseThrow(() -> new IllegalStateException("Role student not found"));

        user.setRole(student);
        userRepository.save(user);
    }

    public void rejectCandidate(Long userId) {
        User user = getCandidate(userId);

        Role listener = roleRepository.findByName("free_listener")
                .orElseThrow(() -> new IllegalStateException("Role free_listener not found"));

        user.setRole(listener);
        userRepository.save(user);
    }

    private User getCandidate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!"candidate".equals(user.getRole().getName())) {
            throw new IllegalStateException("User is not candidate");
        }
        return user;
    }
}
