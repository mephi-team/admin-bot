package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}

