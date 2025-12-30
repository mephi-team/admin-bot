package team.mephi.adminbot.vaadin.users.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserCountService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserCountService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Map<String, Long> getAllCounts() {
        return userRepository.countsByRole();
    }

    public Optional<SimpleUser> findById(Long id) {
        return userRepository.findSimpleUserById(id).map(u -> SimpleUser.builder()
                .id(u.getId())
                .role(u.getRole().getName())
                .fullName(u.getUserName())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .tgId(u.getTgId())
                .status(u.getStatus().name())
                .build());
    }

    @Transactional
    public SimpleUser save(SimpleUser dto) {
        User user = dto.getId() != null
                ? userRepository.findById(dto.getId()).orElse(new User())
                : new User();

        user.setRole(Role.builder().code(dto.getRole()).build());
        user.setUserName(dto.getFirstName() + " " + dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTgId());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (Objects.isNull(user.getStatus())){
            user.setStatus(UserStatus.ACTIVE);
        }

        user = userRepository.save(user);

        return SimpleUser.builder()
                .id(user.getId())
                .role(user.getRole().getCode())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .tgId(user.getTgId())
                .phoneNumber(user.getPhoneNumber())
                .pdConsent(user.getPdConsent())
                .fullName(user.getUserName())
                .build();
    }
}
