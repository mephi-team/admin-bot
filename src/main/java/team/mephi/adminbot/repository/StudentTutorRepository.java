package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.StudentTutor;

/**
 * Репозиторий для управления сущностями StudentTutor.
 */
@Repository
@SuppressWarnings("unused")
public interface StudentTutorRepository extends JpaRepository<StudentTutor, Long> {
}
