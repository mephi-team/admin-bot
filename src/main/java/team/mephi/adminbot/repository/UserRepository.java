package team.mephi.adminbot.repository;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction WHERE u.role.code = :role")
    List<User> findAllByRole(String role);

    @Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction LEFT JOIN FETCH u.tutorAssignments ta LEFT JOIN FETCH ta.tutor LEFT JOIN FETCH u.pdConsentLogs WHERE u.role.code = :role AND (" +
            "LOWER(COALESCE(u.userName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.externalId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.tgId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")")
    List<User> findAllByRoleAndName(String role, String query, Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.direction JOIN FETCH u.role LEFT JOIN u.tutorAssignments ta  " +
            "WHERE (u.role.code ILIKE :role OR  :role IS NULL) " +
            "AND (u.cohort ilike :cohort OR :cohort IS NULL)" +
            "AND (u.direction.id = :direction OR :direction IS NULL)" +
            "AND (u.city ilike :city OR :city IS NULL)" +
            "AND ((ta.tutor.id = :tutor AND ta.isActive) OR :tutor IS NULL)")
    List<User> findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(String role, String cohort, Long direction, String city, Long tutor);

    @Query("SELECT count(u) FROM User u JOIN u.role WHERE u.role.code = :role AND (" +
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

    @Query("SELECT count(u) FROM User u WHERE u.role.code = :role")
    Integer countByRole(String role);

    @Query("SELECT u.role.code, count(u) FROM User u GROUP BY u.role.code UNION ALL SELECT 'TUTOR', count(t) FROM Tutor t")
    List<Tuple> countsByRoleTuples();

    default Map<String, Long> countsByRole() {
        return countsByRoleTuples().stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }

    @Query("SELECT u FROM User u JOIN FETCH u.role JOIN FETCH u.direction WHERE u.id = :id")
    Optional<User> findByIdWithRoleAndDirection(@NonNull Long id);

    List<User> findAllByRoleCode(String role);
    Optional<User> findByRoleCodeAndUserName(String role, String name);
}
