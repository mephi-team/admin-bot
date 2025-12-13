package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти всех пользователей с указанным статусом.
     *
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
     *
     * Ищет совпадения сразу по нескольким полям:
     * - fullName
     * - name (устаревшее)
     * - firstName (устаревшее)
     * - lastName (устаревшее)
     * - externalId (устаревшее)
     * - tgId
     * - email
     * - phoneNumber
     *
     * Поиск нечувствителен к регистру.
     */
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(COALESCE(u.fullName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
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
     *
     * Сначала фильтрует по статусу,
     * затем ищет по тем же полям, что и searchAll().
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND (" +
            "LOWER(COALESCE(u.fullName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
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
}
