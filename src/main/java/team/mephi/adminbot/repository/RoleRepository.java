package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mephi.adminbot.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
