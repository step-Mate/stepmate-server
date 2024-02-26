package server.stepmate.mission.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionProgressDto;
import server.stepmate.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private Integer stepCurrentValue;

    private Integer calorieCurrentValue;

    private boolean isComplete;

    public void missionComplete() {
        this.isComplete = true;
    }

    public void addCurrentValue(Integer stepValue, Integer calorieValue ) {
        this.stepCurrentValue += stepValue;
        this.calorieCurrentValue += calorieValue;
    }

    public MissionDto getMissionDto() {
        return MissionDto.builder()
                .title(this.mission.getTitle())
                .designation(this.mission.getDesignation())
                .contents(this.mission.getContents())
                .missionType(this.mission.getMissionType())
                .stepCurrentValue(this.stepCurrentValue)
                .calorieCurrentValue(this.calorieCurrentValue)
                .isComplete(this.isComplete)
                .currentValue(this.currentValue)
                .goal(this.mission.getGoal())
                .build();
    }

    public MissionProgressDto getMissionProgressDto() {
        return MissionProgressDto.builder()
                .title(this.mission.getTitle())
                .designation(this.mission.getDesignation())
                .contents(this.mission.getContents())
                .missionType(this.mission.getMissionType())
                .stepCurrentValue(this.stepCurrentValue)
                .calorieCurrentValue(this.calorieCurrentValue)
                .goal(this.mission.getGoal())
                .build();
    }

}
