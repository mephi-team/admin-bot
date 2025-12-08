package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mephi.adminbot.model.Broadcast;

import java.util.List;

public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
    List<Broadcast> findAllByOrderByCreatedAtDesc();
}