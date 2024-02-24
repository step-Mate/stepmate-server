package server.stepmate.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionProgressDto;
import server.stepmate.user.entity.DailyStep;

import java.util.List;

/**
 * title을 유저 정보에
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    private Integer ranking;

    private Integer rankChange;

    private String nickname;

    private Integer level;

    private Integer totalStep;

    private String title;

    private List<DailyStepDto> dailySteps;

    private List<MissionProgressDto> missions;

}
