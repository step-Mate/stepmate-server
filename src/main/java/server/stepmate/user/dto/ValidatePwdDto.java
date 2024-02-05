package server.stepmate.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidatePwdDto {
    String password;
}
