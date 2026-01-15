package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.stream.Stream;

/**
 * Сервис для управления экспертами.
 */
public interface ExpertService {
    /**
     * Сохраняет информацию о пользователе.
     *
     * @param dto объект SimpleUser для сохранения.
     * @return сохраненный объект SimpleUser.
     */
    SimpleUser save(SimpleUser dto);

    /**
     * Находит всех пользователей по роли и имени с пагинацией.
     *
     * @param role     роль пользователя.
     * @param query    строка запроса для поиска по имени пользователя.
     * @param pageable объект Pageable для пагинации результатов.
     * @return поток пользователей, соответствующих критериям поиска и пагинации.
     */
    Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable);

    /**
     * Подсчитывает количество пользователей по роли и имени.
     *
     * @param role  роль пользователя.
     * @param query строка запроса для поиска по имени пользователя.
     * @return количество пользователей, соответствующих критериям поиска.
     */
    Integer countByRoleAndName(String role, String query);

    /**
     * Удаляет всех пользователей по их идентификаторам.
     *
     * @param ids коллекция идентификаторов пользователей для удаления.
     */
    void deleteAllById(Iterable<Long> ids);
}
