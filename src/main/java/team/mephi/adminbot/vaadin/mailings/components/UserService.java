package team.mephi.adminbot.vaadin.mailings.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers(Pageable pageable, String query);
    Optional<UserDto> getById(Long id);
}
