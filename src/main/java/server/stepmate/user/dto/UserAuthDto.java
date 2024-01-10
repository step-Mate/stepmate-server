package server.stepmate.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * 회원가입에서 키 나이 성별 다 받아서 할껀지 아니면 나중에 따로 회원정보를 만드는 어떤 것을 할건지 정한다음
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Length(min = 4,max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,12}$")
    private String userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{8,16}$")
    private String password;

    @Length(min = 2, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9!-*]{2,10}$")
    private String nickname;

    @Email
    private String email;

    @NotBlank
    private String age;

    @NotBlank
    private String height;

    @NotBlank
    private String weight;

}