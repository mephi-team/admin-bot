package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.mephi.adminbot.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);

    @Query("SELECT u FROM User u JOIN fetch u.role")
    List<User> findAllWithRoles();
}
