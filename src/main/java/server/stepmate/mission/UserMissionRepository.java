package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.mission.entity.UserMission;

public interface UserMissionRepository extends JpaRepository<UserMission,Long> {
}
