package server.stepmate.rank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRankDto {

    private Integer ranking;

    private Integer rankChange;

    private Integer level;

    private String nickname;

    private Integer monthStep;

    private String title;

}
