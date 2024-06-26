package server.stepmate.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.rank.entity.Rank;
import server.stepmate.user.dto.BodyInfoEncrytDto;
import server.stepmate.user.dto.UserAuthDto;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String password;

    private String email;

    private String nickname;

    private String age;

    private String height;

    private String weight;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private Integer level;

    private double XP;

    private Integer totalStep;

    private Integer monthStep;

    private Integer todayStep;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DailyStep> dailySteps = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<FriendRequest> sentFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<FriendRequest> receivedFriendRequests = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Friendship> friendships = new ArrayList<>();

    private String title;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMission> userMissions;

    public static User createUser(UserAuthDto dto, BodyInfoEncrytDto bodyInfoEncrytDto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .age(bodyInfoEncrytDto.getAge())
                .height(bodyInfoEncrytDto.getHeight())
                .weight(bodyInfoEncrytDto.getWeight())
                .level(1)
                .XP(0)
                .totalStep(0)
                .monthStep(0)
                .todayStep(0)
                .role(ROLE_USER)
                .title("뉴비")
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

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBodyInfo(String age, String height, String weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDesignation(String title) {
        this.title = title;
    }

    public void initUserMissions(List<UserMission> userMissions) {
        this.userMissions = userMissions;
    }

    public void updateStep(Integer step) {
        todayStep += step;
        monthStep += step;
        totalStep += step;
    }

    public void updateXp(double xp) {
        this.XP+=xp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredXp = calculateRequiredXp();
        if (this.XP >= requiredXp) {
            this.level++;
            this.XP -= requiredXp;
            checkLevelUp();
        }
    }

    private int calculateRequiredXp() {
        return this.level * 10;
    }

}
