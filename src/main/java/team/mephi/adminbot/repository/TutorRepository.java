package team.mephi.adminbot.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query(value = """
            SELECT
                t.id, t.first_name, t.last_name, t.tg_id, t.email,
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
}
