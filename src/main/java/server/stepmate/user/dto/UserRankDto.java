package server.stepmate.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRankDto {

    private String nickname;

    private Integer monthStep;

    // 칭호, 뱃지 미정

}
