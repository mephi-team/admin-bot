package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.StatsCache;

@Repository
@SuppressWarnings("unused")
public interface StatsCacheRepository extends JpaRepository<StatsCache, StatsCache.StatsCacheId> {
}

