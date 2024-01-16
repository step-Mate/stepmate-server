package server.stepmate.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.user.dto.UserAuthDto;
import server.stepmate.user.entity.enumtypes.RoleType;

import static server.stepmate.user.entity.enumtypes.RoleType.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String password;

    private String email;

    private String nickname;

    private Integer age;

    private Integer height;

    private Integer weight;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public static User createUser(UserAuthDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .age(dto.getAge())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .role(ROLE_USER)
                .build();
    }

    public UserAuthDto getUserAuthDto() {
        return UserAuthDto.builder()
                .id(this.id)
                .userId(this.userId)
                .nickname(this.nickname)
                .email(this.email)
                .age(this.age)
                .height(this.height)
                .weight(this.weight)
                .build();
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
