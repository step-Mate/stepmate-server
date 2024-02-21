package server.stepmate.rank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRankDto {

    private Integer ranking;

    private Integer level;

    private String nickname;

    private Integer monthStep;

    private String title;

}
