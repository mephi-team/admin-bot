package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MailingService {
    SimpleMailing save(SimpleMailing mailing);
    Optional<SimpleMailing> findById(Long id);
    Stream<SimpleMailing> findMailingByName(String name, List<String> statuses, Pageable pageable);
    Integer countByName(String name, List<MailingStatus> statuses);
    void deleteAllById(Iterable<Long> ids);
}
