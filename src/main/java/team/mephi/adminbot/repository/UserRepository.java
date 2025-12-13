package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);
    java.util.Optional<User> findByExternalId(String externalId);
    List<User> findByRoleName(String name);

    @Query("""
    select u from User u
    where lower(u.name) like lower(concat('%', :q, '%'))
       or lower(u.externalId) like lower(concat('%', :q, '%'))
""")
    List<User> searchAll(@Param("q") String q);

    @Query("""
    select u from User u
    where u.role.name = :role
      and (
           lower(u.name) like lower(concat('%', :q, '%'))
        or lower(u.externalId) like lower(concat('%', :q, '%'))
      )
""")
    List<User> searchByRole(@Param("role") String role,
                            @Param("q") String q);
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
