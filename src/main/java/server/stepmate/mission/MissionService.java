package server.stepmate.mission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionTitleDto;
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

    public List<MissionDto> getMissions(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<UserMission> userMissions = userMissionRepository.findAllById(user.getId());

        return getMissionDtoList(userMissions);
    }

    public List<MissionTitleDto> getUserTitle(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<String> titleList = userMissionRepository.findByUserMissionTitle(user.getId());
        return getMissionTitleDtoList(titleList);
    }

    private  List<MissionTitleDto> getMissionTitleDtoList(List<String> titleList) {
        return titleList.stream()
                .map(MissionTitleDto::new)
                .toList();
    }

    public List<MissionDto> getMissionDtoList(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(UserMission::getMissionDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void completeMission(String title, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        userMissionRepository.updateUserMissionByMission_Title(user.getId(),title);
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
