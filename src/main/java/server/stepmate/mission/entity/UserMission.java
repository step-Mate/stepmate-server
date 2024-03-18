package server.stepmate.mission.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.dto.MissionDetailDto;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionProgressDto;
import server.stepmate.mission.entity.enumtypes.MissionType;
import server.stepmate.user.entity.User;

import java.util.ArrayList;
import java.util.List;

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

    private double calorieCurrentValue;

    private boolean isComplete;

    public void missionComplete() {
        this.isComplete = true;
    }

    public void addCurrentValue(Integer stepValue, double weight ) {
        this.stepCurrentValue += stepValue;
        this.calorieCurrentValue = getCalories(weight);
    }

    private double getCalories(double weight) {
        double calorie = 3.0 * (3.5 * weight * this.stepCurrentValue * 0.0008 * 15) * 5 / 1000;
        double calories = ((int) (calorie * 100)) / 100.0;
        return calories;
    }


    public MissionDto getMissionDto() {

        List<MissionDetailDto> missionDetailDtoList = new ArrayList<>();

        MissionDetailDto stepMissionDetailDto = MissionDetailDto.builder()
                .missionType(MissionType.STEP)
                .currentValue(this.stepCurrentValue)
                .goal(this.mission.getGoal())
                .build();

        MissionDetailDto calorieMissionDetailDto = MissionDetailDto.builder()
                .missionType(MissionType.CALORIE)
                .currentValue(this.calorieCurrentValue)
                .goal(this.mission.getGoal())
                .build();

        if (this.mission.getMissionType() == MissionType.INTEGRATED) {
            missionDetailDtoList.add(stepMissionDetailDto);
            missionDetailDtoList.add(calorieMissionDetailDto);
        }

        if (this.mission.getMissionType() == MissionType.STEP) {
            missionDetailDtoList.add(stepMissionDetailDto);
        }

        if (this.mission.getMissionType() == MissionType.CALORIE) {
            missionDetailDtoList.add(calorieMissionDetailDto);
        }

        return MissionDto.builder()
                .title(this.mission.getTitle())
                .designation(this.mission.getDesignation())
                .contents(this.mission.getContents())
                .detail(missionDetailDtoList)
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
