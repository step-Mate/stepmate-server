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

    @Enumerated(EnumType.STRING)
    private MissionType missionType;

    @Enumerated(EnumType.STRING)
    private MissionCycle missionCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserMission userMission;

    private String contents;

    private Integer goal;

    private boolean isComplete;

    private String reward;
}
