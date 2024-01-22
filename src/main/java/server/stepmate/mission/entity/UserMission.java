package server.stepmate.mission.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMission {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private boolean isComplete;

    public MissionDto getMissionDto() {
        return MissionDto.builder()
                .id(this.id)
                .goal(this.mission.getReward())
                .reward(this.mission.getReward())
                .build();
    }

}
