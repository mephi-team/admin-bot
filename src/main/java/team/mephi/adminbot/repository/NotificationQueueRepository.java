package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.NotificationQueue;

@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {
}

