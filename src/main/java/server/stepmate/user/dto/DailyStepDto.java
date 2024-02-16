package server.stepmate.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStepDto {

    private Integer step;

    private LocalDate date;
}
