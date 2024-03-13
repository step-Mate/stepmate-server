package server.stepmate.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.mission.entity.UserMission;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    @Query("SELECT i from UserMission i WHERE i.user.id = :id and i.isComplete = false and i.mission.missionCycle = 'NONE' group by i.mission.missionType order by MIN(i.mission.id)")
    List<UserMission> findTop5ByUserMission(@Param("id") Long id);

    @Query("update UserMission u set u.isComplete=false, u.stepCurrentValue=0, u.calorieCurrentValue=0 where u.mission.missionCycle='WEEKLY'")
    @Modifying(clearAutomatically = true)
    void resetUserMissionTypeWeekly();

    @Query("update UserMission u set u.isComplete=false, u.stepCurrentValue=0, u.calorieCurrentValue=0 where u.mission.missionCycle='MONTHLY'")
    @Modifying(clearAutomatically = true)
    void resetUserMissionTypeMonthly();

    @Query("select u.mission.designation from UserMission u where u.user.id=:id and u.isComplete = true and u.mission.missionCycle='NONE'")
    List<String> findByUserMissionDesignation(@Param("id") Long id);

    @Query("select u.mission.designation from UserMission u where u.user.id=:id and u.isComplete = true and u.mission.missionCycle='WEEKLY' and u.mission.missionType='STEP'")
    List<String> findByUserMissionWeeklyDesignation(@Param("id") Long id);


    @Query("select u from UserMission u where u.user.id = :id and u.mission.missionCycle = 'NONE'")
    List<UserMission> findAllNoneMissionById(@Param("id") Long id);

    @Query("select u from UserMission u where u.user.id = :id and u.mission.missionCycle = 'WEEKLY' and u.mission.missionType = 'STEP' ")
    List<UserMission> findAllWeeklyStepMissionById(@Param("id") Long id);

    @Query("select u from UserMission u where u.user.id = :id and u.mission.missionCycle = 'WEEKLY' and u.mission.missionType = 'CALORIE'")
    List<UserMission> findAllWeeklyCalorieMissionById(@Param("id") Long id);

    @Query("select u from UserMission u where u.user.id = :id and u.isComplete = false")
    List<UserMission> findAllProgressMissionById(@Param("id") Long id);

    @Query("update UserMission u set u.isComplete=true where u.user.id=:id and u.mission.designation=:designation")
    @Modifying(clearAutomatically = true)
    void updateUserMissionByMission_Title(@Param("id") Long id, @Param("designation") String designation);
}
