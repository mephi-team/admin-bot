package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserService {
    List<UserDto> getAllUsers();

    List<UserDto> getAllUsers(Pageable pageable, String query);

    Optional<UserDto> getById(Long id);

    Map<String, Long> getAllCounts();

    Optional<SimpleUser> findById(Long id);

    SimpleUser save(SimpleUser dto);

    void deleteAllById(Iterable<Long> ids);

    void blockAllById(Iterable<Long> ids);

    Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable);

    Stream<SimpleUser> findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(String role, String cohort, Long direction, String city, Long tutor, Pageable pageable);

    Integer countByRoleAndName(String role, String query);

    List<UserDto> findAllCurators(Pageable pageable, String s);

    Optional<UserDto> findCuratorByUserName(String name);

    Stream<SimpleUser> findAllForCuratorship(String name, Pageable pageable);

    Integer countAllForCuratorship(String name);
}
