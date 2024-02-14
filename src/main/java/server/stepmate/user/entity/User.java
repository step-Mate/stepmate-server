package server.stepmate.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.rank.entity.Rank;
import server.stepmate.user.dto.UserAuthDto;
import server.stepmate.user.dto.UserRankDto;
import server.stepmate.user.entity.enumtypes.RoleType;

import java.util.ArrayList;
import java.util.List;

import static server.stepmate.user.entity.enumtypes.RoleType.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User {

    private static final Integer EXPERIENCE_FOR_LEVEL_UP = 100;

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

    private Integer level;

    private Integer XP;

    private Integer totalStep;

    private Integer monthStep;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<DailyStep> dailySteps = new ArrayList<>();

    private String title;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMission> userMissions;

    public static User createUser(UserAuthDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .age(dto.getAge())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .level(0)
                .XP(0)
                .totalStep(0)
                .monthStep(0)
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

    public Rank getUserRank() {
        return Rank.builder()
                .nickname(this.nickname)
                .monthStep(this.monthStep)
                .level(this.level)
                .title(this.title)
                .build();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void initUserMissions(List<UserMission> userMissions) {
        this.userMissions = userMissions;
    }

    public void updateStep(Integer step) {
        monthStep += step;
        totalStep += step;
    }

    public void calculateLevel() {
        this.level = this.XP / EXPERIENCE_FOR_LEVEL_UP;
    }

    public Integer getCurrentXP() {
        return this.XP % EXPERIENCE_FOR_LEVEL_UP;
    }
}
