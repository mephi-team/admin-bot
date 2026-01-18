package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {
    /**
     * Получает всех пользователей.
     *
     * @return Список всех пользователей.
     */
    List<UserDto> getAllUsers();

    /**
     * Получает всех пользователей с пагинацией и возможностью поиска по запросу.
     *
     * @param pageable Объект Pageable для пагинации результатов.
     * @param query    Строка запроса для фильтрации пользователей.
     * @return Список пользователей, соответствующих критериям поиска и пагинации.
     */
    List<UserDto> getAllUsers(Pageable pageable, String query);

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Опциональный пользователь, если он существует.
     */
    Optional<UserDto> getById(Long id);

    /**
     * Получает количество пользователей по ролям.
     *
     * @return Карта с ролями и соответствующим количеством пользователей.
     */
    Map<String, Long> getAllCounts();

    /**
     * Находит пользователя по id.
     *
     * @param id пользователя.
     * @return опциональный объект SimpleUser.
     */
    Optional<SimpleUser> findById(Long id);

    /**
     * Сохраняет информацию о пользователе.
     *
     * @param dto объект SimpleUser для сохранения.
     * @return сохраненный объект SimpleUser.
     */
    SimpleUser save(SimpleUser dto);

    /**
     * Удаляет всех пользователей по их идентификаторам.
     *
     * @param ids коллекция идентификаторов пользователей для удаления.
     */
    void deleteAllById(Iterable<Long> ids);

    /**
     * Блокирует всех пользователей по их идентификаторам.
     *
     * @param ids коллекция идентификаторов пользователей для блокировки.
     */
    void blockAllById(Iterable<Long> ids);

    /**
     * Разблокирует всех пользователей по их идентификаторам.
     *
     * @param ids коллекция идентификаторов пользователей для разблокировки.
     */
    void unblockAllById(Iterable<Long> ids);

    /**
     * Находит пользователей по роли и имени с пагинацией.
     *
     * @param role     роль пользователя.
     * @param query    имя пользователя.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток пользователей, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable);

    /**
     * Находит пользователей по нескольким критериям с пагинацией.
     *
     * @param role      роль пользователя.
     * @param cohort    когорта пользователя.
     * @param direction направление пользователя.
     * @param city      город пользователя.
     * @param tutor     куратор пользователя.
     * @param pageable  объект Pageable для пагинации результатов.
     * @return поток пользователей, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleUser> findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(String role, String cohort, Long direction, String city, Long tutor, Pageable pageable);

    /**
     * Подсчитывает количество пользователей по роли и имени.
     *
     * @param role  роль пользователя.
     * @param query имя пользователя.
     * @return количество пользователей, соответствующих критериям поиска.
     */
    Integer countByRoleAndName(String role, String query);

    /**
     * Находит пользователей по строке с пагинацией.
     *
     * @param pageable объект Pageable для пагинации результатов.
     * @param query    строка запроса.
     * @return поток пользователей, соответствующих критериям поиска и пагинации.
     */
    List<UserDto> findAllCurators(Pageable pageable, String query);

    /**
     * Подсчитывает количество кураторов по имени.
     *
     * @param name имя пользователя.
     * @return количество кураторов, соответствующих критериям поиска.
     */
    Optional<UserDto> findCuratorByUserName(String name);

    /**
     * Находит пользователей для кураторства по имени с пагинацией.
     *
     * @param name     имя пользователя.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток пользователей, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleUser> findAllForCuratorship(String name, Pageable pageable);

    /**
     * Подсчитывает количество пользователей для кураторства по имени.
     *
     * @param name имя пользователя.
     * @return количество пользователей, соответствующих критериям поиска.
     */
    Integer countAllForCuratorship(String name);

    /**
     * Проверяет существование пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    Boolean existsById(Long id);

    /**
     * Получает всех пользователей в упрощенном виде.
     *
     * @return Список всех пользователей в упрощенном виде.
     */
    List<SimpleUser> findAll();

    /**
     * Подсчитывает общее количество пользователей.
     *
     * @return Общее количество пользователей.
     */
    Long countAllUsers();
}
