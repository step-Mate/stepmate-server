package server.stepmate.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRankDto {

    private Integer rank;

    private Integer level;

    private String nickname;

    private Integer monthStep;

    // 칭호, 뱃지 미정

}
