package server.stepmate.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMyInfoDto {
    private String nickname;
    private Integer level;
    private String title;
}

