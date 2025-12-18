package team.mephi.adminbot.vaadin.mailings.components;

import team.mephi.adminbot.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
}
