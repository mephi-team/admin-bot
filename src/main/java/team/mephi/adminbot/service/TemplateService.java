package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления шаблонами.
 */
public interface TemplateService {
    List<SimpleTemplate> findAll();

    List<SimpleTemplate> findAll(Pageable pageable, String s);

    SimpleTemplate save(SimpleTemplate template);

    Optional<SimpleTemplate> findById(Long id);

    Stream<SimpleTemplate> findAllByName(String name, Pageable pageable);

    Integer countByName(String name);

    void deleteAllById(Iterable<Long> ids);
}
