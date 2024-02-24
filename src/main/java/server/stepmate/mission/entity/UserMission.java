package server.stepmate.mission.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMission {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private boolean isComplete;

    public MissionDto getMissionDto() {
        return MissionDto.builder()
                .title(this.mission.getTitle())
                .designation(this.mission.getDesignation())
                .contents(this.mission.getContents())
                .missionType(this.mission.getMissionType())
                .isComplete(this.isComplete)
                .goal(this.mission.getGoal())
                .isComplete(this.isComplete)
                .missionType(this.mission.getMissionType())
                .build();
    }

}
