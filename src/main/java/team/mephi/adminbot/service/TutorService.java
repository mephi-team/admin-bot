package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.TutorDto;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления репетиторами.
 */
public interface TutorService {
    /**
     * Сохраняет информацию о кураторе.
     *
     * @param dto объект SimpleTutor для сохранения.
     * @return сохраненный объект SimpleTutor.
     */
    SimpleTutor save(SimpleTutor dto);

    /**
     * Находит куратора по id.
     *
     * @param id куратора.
     * @return опциональный объект SimpleTutor.
     */
    Optional<SimpleTutor> findById(Long id);

    /**
     * Удаляет всех кураторов по их идентификаторам.
     *
     * @param ids коллекция идентификаторов кураторов для удаления.
     */
    void deleteAllById(Iterable<Long> ids);

    /**
     * Блокирует всех кураторов по их идентификаторам.
     *
     * @param ids коллекция идентификаторов кураторов для блокировки.
     */
    void blockAllById(Iterable<Long> ids);

    /**
     * Находит кураторов по имени с пагинацией.
     *
     * @param name     имя куратора.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток кураторов, соответствующих критериям поиска и пагинации.
     */
    Stream<TutorDto> findAllByName(String name, Pageable pageable);

    /**
     * Подсчитывает количество кураторов по имени.
     *
     * @param name имя куратора.
     * @return количество кураторов, соответствующих критериям поиска.
     */
    Integer countByName(String name);

    /**
     * Находит всех кураторов с их направлениями и назначенными студентами, соответствующих заданному имени.
     *
     * @param name     имя куратора.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток кураторов с их направлениями и назначенными студентами.
     */
    Stream<SimpleTutor> findAllWithDirectionsAndStudents(String name, Pageable pageable);
}
