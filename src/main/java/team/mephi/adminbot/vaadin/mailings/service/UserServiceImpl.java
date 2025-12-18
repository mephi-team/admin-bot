package team.mephi.adminbot.vaadin.mailings.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.mailings.components.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build()).toList();
    }
}
