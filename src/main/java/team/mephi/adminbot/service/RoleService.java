package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.RoleDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления ролями.
 */
public interface RoleService {
    /**
     * Получает все роли.
     *
     * @return Список всех ролей.
     */
    List<RoleDto> getAllRoles();

    /**
     * Получает все роли с пагинацией и возможностью поиска по запросу.
     *
     * @param pageable Объект Pageable для пагинации результатов.
     * @param query    Строка запроса для фильтрации ролей.
     * @return Список ролей, соответствующих критериям поиска и пагинации.
     */
    List<RoleDto> getAllRoles(Pageable pageable, String query);

    /**
     * Подсчитывает роль по коду.
     *
     * @param code Код роли.
     * @return Роль соответствующую коду.
     */
    Optional<RoleDto> getByCode(String code);

    /**
     * Получает роль по её имени.
     *
     * @param name Имя роли.
     * @return Опциональная роль, если она существует.
     */
    Optional<RoleDto> getByName(String name);
}
