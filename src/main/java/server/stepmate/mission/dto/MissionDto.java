package server.stepmate.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.entity.enumtypes.MissionType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDto {

    private String title;

    private String designation;

    private String contents;

    private Integer goal;

    private boolean isComplete;

    private MissionType missionType;

}
