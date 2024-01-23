package server.stepmate.config.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import server.stepmate.mission.MissionService;
import server.stepmate.user.UserService;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final MissionService missionService;
    private final UserService userService;

    @Scheduled(cron = "0 0 0 ? * MON") // 매주 월요일 자정
    public void initWeek() {
        missionService.resetWeeklyUserMission(); // 주간 미션 초기화
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정
    public void initMonth() {
        userService.resetAllUserMonthStep(); // 월 걸음수 초기화
        missionService.resetMonthlyUserMission(); // 월간 미션 초기화
    }

}
