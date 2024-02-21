package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.mission.entity.UserMission;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    @Query("SELECT i from UserMission i WHERE i.user.id = :id and i.isComplete = false order by i.mission.id asc ")
    List<UserMission> findTop5ByUserMission(@Param("id") Long id);

    @Query("update UserMission u set u.isComplete=false where u.mission.missionCycle='WEEKLY'")
    @Modifying(clearAutomatically = true)
    void resetUserMissionTypeWeekly();

    @Query("update UserMission u set u.isComplete=false where u.mission.missionCycle='MONTHLY'")
    @Modifying(clearAutomatically = true)
    void resetUserMissionTypeMonthly();

    @Query("select u.mission.title from UserMission u where u.user.id=:id and u.isComplete = true")
    List<String> findByUserMissionTitle(@Param("id") Long id);

    @Query("select u from UserMission u where u.user.id = :id")
    List<UserMission> findAllById(@Param("id") Long id);

    @Query("update UserMission u set u.isComplete=true where u.user.id=:id and u.mission.title=:title")
    @Modifying(clearAutomatically = true)
    void updateUserMissionByMission_Title(@Param("id") Long id, @Param("title") String title);
}
