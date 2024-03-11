package server.stepmate.mission.dto;

import lombok.*;
import server.stepmate.mission.entity.enumtypes.MissionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDetailDto {

    private MissionType missionType;

    private double currentValue;

    private Integer goal;
}
