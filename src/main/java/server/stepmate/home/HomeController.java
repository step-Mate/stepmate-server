package server.stepmate.home;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.MissionService;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionTitleDto;
import server.stepmate.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final ResponseService responseService;
    private final MissionService missionService;
    private final UserService userService;

    @GetMapping("/home/missions")
    public DataResponse<List<MissionDto>> getHomeMission(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(missionService.getHomeMission(customUserDetails));
    }

    @GetMapping("/home/titles")
    public DataResponse<List<MissionTitleDto>> getUserTitles(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(missionService.getUserTitle(customUserDetails));
    }

    @PatchMapping("/home/select-title")
    public CommonResponse selectTitle(@RequestBody MissionTitleDto req, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.selectTitle(req.getTitle(), customUserDetails);
        return responseService.getSuccessResponse();
    }


}
