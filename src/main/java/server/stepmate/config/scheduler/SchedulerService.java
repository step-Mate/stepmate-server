package server.stepmate.config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import server.stepmate.mission.MissionService;
import server.stepmate.rank.RankService;
import server.stepmate.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final MissionService missionService;
    private final UserService userService;
    private final RankService rankService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void initDay() {
        rankService.updateRank();
        userService.resetAllUserTodayStep();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " + "initDay 실행");
    }

    @Scheduled(cron = "0 0 0 ? * MON") // 매주 월요일 자정
    public void initWeek() {
        missionService.resetWeeklyUserMission(); // 주간 미션 초기화
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " + "initWeek 실행");
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정
    public void initMonth() {
        userService.resetAllDailyStep();
        userService.resetAllUserMonthStep(); // 월 걸음수 초기화
        missionService.resetMonthlyUserMission(); // 월간 미션 초기화
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " + "initMonth 실행");
    }

}
