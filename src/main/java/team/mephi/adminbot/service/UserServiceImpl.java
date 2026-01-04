package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers(Pageable pageable, String query) {
        return userRepository.searchAll(query)
                .stream()
                .map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build())
                .skip(pageable.getOffset()) // Пропускаем уже загруженные элементы
                .limit(pageable.getPageSize())
                .toList();
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build());
    }

    @Override
    public Map<String, Long> getAllCounts() {
        return userRepository.countsByRole();
    }

    @Override
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
    @Override
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
        user.setCity(dto.getCity());
        user.setDirection(Direction.builder().id(dto.getDirection().getId()).name(dto.getDirection().getName()).build());
        user.setCohort(dto.getCohort());

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
                .city(user.getCity())
                .direction(SimpleDirection.builder().id(user.getDirection().getId()).name(user.getDirection().getName()).build())
                .cohort(user.getCohort())
                .status(user.getStatus().name())
                .build();
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public void blockAllById(Iterable<Long> ids) {
        userRepository.blockAllById(ids);
    }

    @Override
    public Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable) {
        return userRepository.findAllByRoleAndName(role, query, pageable)
                .stream()
                .map(u -> SimpleUser.builder()
                        .id(u.getId())
                        .role(u.getRole().getCode())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .fullName(u.getUserName())
                        .phoneNumber(u.getPhoneNumber())
                        .tgId(u.getTgId())
                        .tgName(u.getTgName())
                        .email(u.getEmail())
                        .phoneNumber(u.getPhoneNumber())
                        .pdConsent(u.getPdConsent())
                        .status(u.getStatus().name())
                        .city(u.getCity())
                        .direction(Objects.nonNull(u.getDirection()) ? SimpleDirection.builder().id(u.getDirection().getId()).name(u.getDirection().getName()).build() : null)
                        .cohort(u.getCohort())
                        .build());
    }

    @Override
    public Integer countByRoleAndName(String role, String query) {
        return userRepository.countByRoleAndName(role, query);
    }

    @Override
    public Optional<UserDto> findByUserName(String name) {
        return userRepository.findByUserName(name).map(u -> UserDto.builder()
                .id(u.getId())
                .userName(u.getUserName())
                .build());
    }
}
