package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mephi.adminbot.model.StudentTutor;

public interface StudentTutorRepository extends JpaRepository<StudentTutor, Long> {
}
