package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.stream.Stream;

public interface ExpertService {
    Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable);
    Integer countByRoleAndName(String role, String query);
    void deleteAllById(Iterable<Long> ids);
}
