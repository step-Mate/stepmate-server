package server.stepmate.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.user.dto.DailyStepDto;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DailyStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Integer steps;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static DailyStep createDailyStep(int step, User user, LocalDate date) {
        return DailyStep.builder()
                .steps(step)
                .user(user)
                .date(date)
                .build();
    }

    public DailyStepDto getDailyStepDto() {
        return DailyStepDto.builder()
                .step(this.steps)
                .date(this.date)
                .build();
    }

    public void addSteps(Integer steps) {
        this.steps+=steps;
    }
}
