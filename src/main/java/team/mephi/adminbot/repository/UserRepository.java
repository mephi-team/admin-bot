package team.mephi.adminbot.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.mephi.adminbot.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);

    @Query("SELECT u FROM User u JOIN fetch u.role")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u JOIN fetch u.role WHERE u.role.name = :role")
    List<User> findAllByRole(String role);

    @Query("SELECT u.role.name, count(u) FROM User u GROUP BY u.role.name UNION ALL SELECT 'tutor', count(t) FROM Tutor t")
    List<Tuple> countsByRoleTuples();

    default Map<String, Long> countsByRole() {
        return countsByRoleTuples().stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }
}
