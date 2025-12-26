package team.mephi.adminbot.repository;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти всех пользователей с указанным статусом.
     * <p>
     * Например: ACTIVE, BLOCKED, EXPELLED и т.д.
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Найти пользователя по старому внешнему ID.
     *
     * @deprecated Устаревший метод. Используй findByTgId.
     */
    @Deprecated
    Optional<User> findByExternalId(String externalId);

    /**
     * Найти пользователя по Telegram ID.
     */
    Optional<User> findByTgId(String tgId);

    /**
     * Найти пользователя по email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Найти пользователя по номеру телефона.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Поиск пользователей по строке запроса.
     * <p>
     * Ищет совпадения сразу по нескольким полям:
     * - fullName
     * - name (устаревшее)
     * - firstName (устаревшее)
     * - lastName (устаревшее)
     * - externalId (устаревшее)
     * - tgId
     * - email
     * - phoneNumber
     * <p>
     * Поиск нечувствителен к регистру.
     */
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(COALESCE(u.userName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.externalId, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.tgId, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchAll(@Param("q") String query);

    /**
     * Поиск пользователей по статусу и строке запроса.
     * <p>
     * Сначала фильтрует по статусу,
     * затем ищет по тем же полям, что и searchAll().
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND (" +
            "LOWER(COALESCE(u.userName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.externalId, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.tgId, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :q, '%'))" +
            ")")
    List<User> searchByStatus(
            @Param("status") UserStatus status,
            @Param("q") String query
    );

    List<User> findByStatus(String status);

    @Query("SELECT u FROM User u JOIN fetch u.role")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction WHERE u.role.name = :role")
    List<User> findAllByRole(String role);

    //@Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction WHERE u.deleted = false AND u.role.name = :role AND (" +
    @Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction WHERE u.role.name = :role AND (" +
            "LOWER(COALESCE(u.userName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.externalId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.tgId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")")
    List<User> findAllByRoleAndName(String role, String query);

    //@Query("SELECT count(u )FROM User u JOIN u.role WHERE u.deleted = false AND u.role.name = :role AND (" +
    @Query("SELECT count(u )FROM User u JOIN u.role WHERE u.role.name = :role AND (" +
            "LOWER(COALESCE(u.userName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.externalId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.tgId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")")
    Integer countByRoleAndName(String role, String query);

    @Query("update User u set u.deleted = FUNCTION('NOT', u.deleted) WHERE u.id = :id")
    @Transactional
    @Modifying
    void deleteById(@NonNull Long id);

    @Query("update User u set u.deleted = FUNCTION('NOT', u.deleted) WHERE u.id IN :ids")
    @Transactional
    @Modifying
    void deleteAllById(@Param("ids") Iterable<? extends Long> ids);

    @Query("update User u set u.status = team.mephi.adminbot.model.enums.UserStatus.BLOCKED WHERE u.id IN :ids")
    @Transactional
    @Modifying
    void blockAllById(@Param("ids") Iterable<? extends Long> ids);

    @Query("SELECT count(u) FROM User u WHERE u.role.name = :role")
    Integer countByRole(String role);

    @Query("SELECT u.role.name, count(u) FROM User u GROUP BY u.role.name UNION ALL SELECT 'tutor', count(t) FROM Tutor t")
//    @Query("SELECT u.role.name, count(u) FROM User u WHERE u.deleted = false GROUP BY u.role.name UNION ALL SELECT 'tutor', count(t) FROM Tutor t")
    List<Tuple> countsByRoleTuples();

    default Map<String, Long> countsByRole() {
        return countsByRoleTuples().stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }

    @Query("SELECT new team.mephi.adminbot.dto.SimpleUser(u.id, u.role.code, u.firstName, u.lastName, u.email, u.tgId) FROM User u WHERE u.id = :id")
    Optional<SimpleUser> findSimpleUserById(@NonNull Long id);
}
