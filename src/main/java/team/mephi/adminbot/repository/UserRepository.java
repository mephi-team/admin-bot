package team.mephi.adminbot.repository;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.model.enums.UserStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Репозиторий для управления сущностями User.
 */
@Repository
@SuppressWarnings("unused")
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти пользователя по email.
     */
    Optional<User> findByEmail(String email);

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
     * Найти всех пользователей по роли и строке запроса
     * вместе с их ролями, направлениями, кураторами и логами согласий на обработку персональных данных.
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
    @Query("SELECT u FROM User u JOIN fetch u.role LEFT JOIN FETCH u.direction LEFT JOIN FETCH u.tutorAssignments ta LEFT JOIN FETCH ta.tutor LEFT JOIN FETCH u.pdConsentLogs WHERE u.deleted = false AND u.role.code = :role AND (" +
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

    /**
     * Найти всех пользователей по нескольким критериям
     * вместе с их направлениями, активными кураторами и логами согласий на обработку персональных данных.
     * <p>
     * Все параметры являются необязательными фильтрами.
     * Если параметр равен null, фильтрация по нему не применяется.
     */
    @Query("SELECT u FROM User u JOIN FETCH u.direction JOIN FETCH u.role LEFT JOIN u.tutorAssignments ta  " +
            "WHERE (u.role.code ILIKE :role OR  :role IS NULL) " +
            "AND (u.cohort ilike :cohort OR :cohort IS NULL)" +
            "AND (u.direction.id = :direction OR :direction IS NULL)" +
            "AND (u.city ilike :city OR :city IS NULL)" +
            "AND ((ta.tutor.id = :tutor AND ta.isActive) OR :tutor IS NULL)")
    List<User> findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(String role, String cohort, Long direction, String city, Long tutor);

    /**
     * Подсчитать количество пользователей по роли и строке запроса.
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
    @Query("SELECT count(u) FROM User u JOIN u.role WHERE u.deleted = false AND u.role.code = :role AND (" +
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

    /**
     * Логическое удаление нескольких пользователей по их идентификаторам.
     * <p>
     * Меняет значение поля deleted на противоположное.
     */
    @Query("update User u set u.deleted = true WHERE u.id IN :ids")
    @Transactional
    @Modifying
    void deleteAllById(@Param("ids") @NonNull Iterable<? extends Long> ids);

    /**
     * Блокировка нескольких пользователей по их идентификаторам.
     * <p>
     * Устанавливает значение поля status в BLOCKED.
     */
    @Query("update User u set u.status = :status WHERE u.id IN :ids")
    @Transactional
    @Modifying
    void changeStatusById(UserStatus status, @Param("ids") Iterable<? extends Long> ids);

    /**
     * Подсчитать количество пользователей по каждой роли.
     */
    @Query("SELECT u.role.code, count(u) FROM User u WHERE u.deleted = false AND u.role.code != 'LC_EXPERT' GROUP BY u.role.code UNION ALL SELECT 'LC_EXPERT', count(e) FROM Expert e WHERE e.isActive UNION ALL SELECT 'TUTOR', count(t) FROM Tutor t")
    List<Tuple> countsByRoleTuples();

    /**
     * Подсчитать количество пользователей по каждой роли и вернуть в виде карты.
     */
    default Map<String, Long> countsByRole() {
        return countsByRoleTuples().stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }

    /**
     * Найти пользователя по идентификатору вместе с его ролью и направлением.
     */
    @Query("SELECT u FROM User u JOIN FETCH u.role JOIN FETCH u.direction LEFT JOIN FETCH u.tutorAssignments LEFT JOIN FETCH u.pdConsentLogs WHERE u.id = :id AND u.deleted = false")
    Optional<User> findByIdWithRoleAndDirection(@NonNull Long id);

    /**
     * Найти всех пользователей по роли.
     */
    List<User> findAllByRoleCode(String role);

    /**
     * Найти всех студентов по строке запроса,
     * у которых нет активных назначений куратора,
     * вместе с их ролями, направлениями, логами согласий на обработку персональных данных.
     * <p>
     * Ищет совпадения по полю lastName.
     * Поиск нечувствителен к регистру.
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) AND (u.tutorAssignments IS EMPTY OR NOT EXISTS (SELECT 1 FROM StudentTutor st WHERE st.student = u AND st.isActive = true)) AND u.role.code = :role")
    List<User> findAllStudentsWithTutorAssignments(String query, String role, Pageable pageable);

    /**
     * Подсчитать количество студентов по строке запроса,
     * у которых нет активных назначений куратора.
     * <p>
     * Ищет совпадения по полю lastName.
     * Поиск нечувствителен к регистру.
     */
    @Query("SELECT count(u) FROM User u WHERE u.deleted = false AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) AND (u.tutorAssignments IS EMPTY OR NOT EXISTS (SELECT 1 FROM StudentTutor st WHERE st.student = u AND st.isActive = true)) AND u.role.code = :role")
    Integer countAllStudentsWithTutorAssignments(String query, String role);

    /**
     * Подсчитать количество пользователей по идентификатору,
     * у которых есть назначение куратора.
     */
    @Query("SELECT count(u) FROM User u JOIN u.tutorAssignments ta WHERE u.id = :id")
    Integer countByIdWithTutorAssignment(Long id);

    /**
     * Найти всех пользователей вместе с их направлениями,
     * логами согласий на обработку персональных данных и активными кураторами.
     */
    @Query("SELECT u FROM User u JOIN FETCH u.direction LEFT JOIN FETCH u.pdConsentLogs LEFT JOIN FETCH u.tutorAssignments ta LEFT JOIN FETCH ta.tutor WHERE u.deleted = false")
    List<User> findAllWithDirection();

    /**
     * Подсчитать количество всех не удалённых пользователей.
     */
    Long countByDeletedIsFalse();

    Boolean existsByIdAndDeletedIsFalse(Long id);
}
