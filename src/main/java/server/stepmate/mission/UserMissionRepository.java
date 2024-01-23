package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.mission.entity.UserMission;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission,Long> {

    @Query("SELECT i from UserMission i WHERE i.user.id = :id and i.isComplete = false order by i.mission.id asc ")
    List<UserMission> findTop5ByUserMission(@Param("id") Long id);

    @Query("update UserMission u set u.isComplete=false where u.mission.missionCycle='WEEKLY'")
    void resetUserMissionTypeWeekly();

    @Query("update UserMission u set u.isComplete=false where u.mission.missionCycle='MONTHLY'")
    void resetUserMissionTypeMonthly();
}
