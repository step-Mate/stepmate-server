package server.stepmate.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDto {

    private Long id;

    private String contents;

    private Integer goal;

    private Integer reward;

}
