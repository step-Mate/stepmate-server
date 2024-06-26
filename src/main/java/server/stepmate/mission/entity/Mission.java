package server.stepmate.mission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.entity.enumtypes.MissionCycle;
import server.stepmate.mission.entity.enumtypes.MissionType;
import server.stepmate.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mission {

    @Id
    private Long id;

    private String title;

    private String designation;

    @Enumerated(EnumType.STRING)
    private MissionType missionType; //STEP,CALORIE

    @Enumerated(EnumType.STRING)
    private MissionCycle missionCycle; //WEEKLY,MONTHLY,NONE

    private String contents;

    private Integer goal;

    private Integer reward;
}
