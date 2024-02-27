package server.stepmate.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBodyInfoDto {

    @NotNull
    private Integer age;

    @NotNull
    private Integer height;

    @NotNull
    private Integer weight;
}
