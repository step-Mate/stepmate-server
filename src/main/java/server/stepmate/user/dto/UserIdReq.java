package server.stepmate.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserIdReq {
    @NotBlank
    @Length(min = 4,max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,12}$")
    private String userId;
}
