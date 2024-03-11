package server.stepmate.mission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.dto.MissionDetailDto;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionProgressDto;
import server.stepmate.mission.dto.MissionDesignationDto;
import server.stepmate.mission.entity.Mission;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.mission.entity.enumtypes.MissionType;
import server.stepmate.user.UserRepository;
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
    private final UserRepository userRepository;

    public List<MissionDto> getMissions(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        List<MissionDto> missionDtoList = new ArrayList<>();

        List<UserMission> userMissions = userMissionRepository.findAllNoneMissionById(user.getId());
        List<UserMission> weeklyStepMission = userMissionRepository.findAllWeeklyStepMissionById(user.getId());
        List<UserMission> weeklyCalorieMission = userMissionRepository.findAllWeeklyCalorieMissionById(user.getId());

        for (int i = 0; i < weeklyStepMission.size(); i++) {

            List<MissionDetailDto> missionDetailDtoList = new ArrayList<>();

            MissionDetailDto stepBuild = MissionDetailDto.builder()
                    .missionType(MissionType.STEP)
                    .currentValue(weeklyStepMission.get(i).getStepCurrentValue())
                    .goal(weeklyStepMission.get(i).getMission().getGoal())
                    .build();

            MissionDetailDto calorieBuild = MissionDetailDto.builder()
                    .missionType(MissionType.CALORIE)
                    .currentValue(weeklyCalorieMission.get(i).getCalorieCurrentValue())
                    .goal(weeklyCalorieMission.get(i).getMission().getGoal())
                    .build();

            missionDetailDtoList.add(stepBuild);
            missionDetailDtoList.add(calorieBuild);

            MissionDto missionDto = MissionDto.builder()
                    .title(weeklyStepMission.get(i).getMission().getTitle())
                    .contents(weeklyStepMission.get(i).getMission().getContents() + weeklyCalorieMission.get(i).getMission().getContents())
                    .designation(weeklyStepMission.get(i).getMission().getDesignation())
                    .detail(missionDetailDtoList)
                    .build();

            missionDtoList.add(missionDto);
        }

        List<MissionDto> missionDtoList2 = getMissionDtoList(userMissions);
        missionDtoList.addAll(missionDtoList2);

        return missionDtoList;
    }

    public List<MissionDesignationDto> getUserDesignations(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<String> titleList = userMissionRepository.findByUserMissionDesignation(user.getId());
        return getMissionTitleDtoList(titleList);
    }

    private  List<MissionDesignationDto> getMissionTitleDtoList(List<String> titleList) {
        return titleList.stream()
                .map(MissionDesignationDto::new)
                .toList();
    }

    public List<MissionProgressDto> getMissionProgressDtoList(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(UserMission::getMissionProgressDto)
                .collect(Collectors.toList());
    }

    public List<MissionDto> getMissionDtoList(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(UserMission::getMissionDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void completeMission(String designation, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        userMissionRepository.updateUserMissionByMission_Title(user.getId(),designation);
        Mission mission = missionRepository.findMissionByDesignation(designation).orElseThrow(() -> new CustomException(CustomExceptionStatus.REQUEST_ERROR));
        user.updateXp(mission.getReward());
        userRepository.save(user);
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
