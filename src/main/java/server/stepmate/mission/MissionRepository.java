package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.mission.entity.Mission;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findMissionByTitle(String title);
}
