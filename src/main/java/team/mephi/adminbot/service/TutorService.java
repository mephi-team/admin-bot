package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.Optional;
import java.util.stream.Stream;

public interface TutorService {
    SimpleUser save(SimpleUser dto);
    Optional<SimpleUser> findById(Long id);
    void deleteAllById(Iterable<Long> ids);
    void blockAllById(Iterable<Long> ids);
    Stream<SimpleUser> findAllByName(String name, Pageable pageable);
    Integer countByName(String name);
}
