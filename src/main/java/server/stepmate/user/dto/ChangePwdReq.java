package server.stepmate.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePwdReq {

    @NotBlank
    private String userId;

    @NotBlank
    @Length(min = 8, max = 16)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{8,16}$")
    private String password;

}
