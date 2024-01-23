package server.stepmate.mission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    public List<MissionDto> getHomeMission(CustomUserDetails customUserDetails) {
        List<MissionDto> missionDtoList = new ArrayList<>();
        User user = customUserDetails.getUser();
        List<UserMission> userMissions = userMissionRepository.findTop5ByUserMission(user.getId());

        return getMissionDtoList(userMissions);
    }

    private List<MissionDto> getMissionDtoList(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(UserMission::getMissionDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void resetWeeklyUserMission() {
        userMissionRepository.resetUserMissionTypeWeekly();
    }

    @Transactional
    public void resetMonthlyUserMission() {
        userMissionRepository.resetUserMissionTypeMonthly();
    }
}
