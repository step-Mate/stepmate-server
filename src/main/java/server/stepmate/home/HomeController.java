package server.stepmate.home;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.MissionService;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionTitleDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final ResponseService responseService;
    private final MissionService missionService;

    @GetMapping("/home/missions")
    public DataResponse<List<MissionDto>> getHomeMission(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(missionService.getHomeMission(customUserDetails));
    }

    @GetMapping("/home/titles")
    public DataResponse<List<MissionTitleDto>> getUserTitles(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(missionService.getUserTitle(customUserDetails));
    }


}
