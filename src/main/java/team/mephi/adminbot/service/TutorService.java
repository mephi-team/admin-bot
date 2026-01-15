package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleTutor;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления репетиторами.
 */
public interface TutorService {
    SimpleTutor save(SimpleTutor dto);

    Optional<SimpleTutor> findById(Long id);

    void deleteAllById(Iterable<Long> ids);

    void blockAllById(Iterable<Long> ids);

    Stream<SimpleTutor> findAllByName(String name, Pageable pageable);

    Integer countByName(String name);
}
