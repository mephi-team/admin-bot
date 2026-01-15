package team.mephi.adminbot.repository;

import jakarta.persistence.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Role;

import java.util.Optional;

/**
 * Репозиторий для работы с ролями пользователей.
 *
 * <p>Роли являются справочными данными и обычно загружаются по коду (code).
 */
@Repository
@Cacheable
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, String> {
    /**
     * Находит роль по названию.
     *
     * @param name название роли
     * @return роль с указанным названием или пустой Optional
     */
    Optional<Role> findByName(String name);

    /**
     * Находит роль по коду.
     *
     * @param code код роли
     * @return роль с указанным кодом или пустой Optional
     */
    Optional<Role> findByCode(String code);
}
