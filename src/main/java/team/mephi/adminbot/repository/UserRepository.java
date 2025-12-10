package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.externalId) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchAll(@Param("q") String query);
    @Query("SELECT u FROM User u WHERE u.status = :status AND (" +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.externalId) LIKE LOWER(CONCAT('%', :q, '%'))" +
            ")")
    List<User> searchByStatus(
     @Param("status") String status,
     @Param("q") String query
    );
}
