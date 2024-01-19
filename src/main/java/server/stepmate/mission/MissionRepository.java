package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.mission.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
