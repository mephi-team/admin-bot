package team.mephi.adminbot.vaadin.mailings.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.mailings.components.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

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
        System.out.println("! id " + id);
        return userRepository.findById(id).map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build());
    }
}
