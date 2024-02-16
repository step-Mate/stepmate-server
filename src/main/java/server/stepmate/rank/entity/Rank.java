package server.stepmate.rank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.rank.dto.UserRankDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private Integer level;

    private Integer monthStep;

    private String title;

    private Integer ranking;

    private Integer rankChange;

    public void updateRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public void calculateRankChange(Integer rankChange){
        this.rankChange=rankChange;
    }

    public UserRankDto getUserRankDto() {
        return UserRankDto.builder()
                .nickname(this.nickname)
                .level(this.level)
                .monthStep(this.monthStep)
                .title(this.title)
                .ranking(this.ranking)
                .rankChange(this.rankChange)
                .build();
    }
}
