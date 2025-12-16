package team.mephi.adminbot.repository;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;

import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query(value = """
                    SELECT
                        t.id, CONCAT(t.first_name, ' ', t.last_name), t.tg_id, t.email, t.deleted,
                        COUNT(DISTINCT st.id) AS student_count,
                        STRING_AGG(d.name, ', ') AS directions
                    FROM tutors t
                             LEFT JOIN student_tutor st ON st.tutor_id = t.id
                             LEFT JOIN tutor_directions td ON td.tutor_id = t.id
                             LEFT JOIN directions d ON td.direction_id = d.id
                    WHERE LOWER(t.last_name) LIKE LOWER(CONCAT('%', :query, '%')) OR 
                              LOWER(t.first_name) LIKE LOWER(CONCAT('%', :query, '%'))
                    GROUP BY t.id
            """, nativeQuery = true)
    List<TutorWithCounts> findAllWithDirectionsAndStudents(String query);

    @Query("SELECT count(t) FROM Tutor t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', :query, '%')) or LOWER(t.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByName(String query);

    @Query("update Tutor t set t.deleted = FUNCTION('NOT', t.deleted) WHERE t.id = :id")
    @Transactional
    @Modifying
    void deleteById(@NonNull Long id);

    @Query("update Tutor t set t.deleted = FUNCTION('NOT', t.deleted) WHERE t.id IN :ids")
    @Transactional
    @Modifying
    void deleteAllById(@Param("ids") Iterable<? extends Long> ids);

    @Query("SELECT new team.mephi.adminbot.dto.SimpleUser(u.id, 'tutor', u.firstName, u.lastName, u.email, u.tgId) FROM Tutor u WHERE u.id = :id")
    Optional<SimpleUser> findSimpleUserById(@NonNull Long id);
}
