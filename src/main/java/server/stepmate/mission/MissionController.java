package server.stepmate.mission;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.mission.dto.MissionDto;
import server.stepmate.mission.dto.MissionTitleDto;
import server.stepmate.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final ResponseService responseService;
    private final MissionService missionService;
    private final UserService userService;

    @Operation(summary = "보유하고 있는 칭호 조회 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/titles")
    public List<MissionTitleDto> getUserTitles(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return missionService.getUserTitle(customUserDetails);
    }

    @Operation(summary = "칭호 선택 API", security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/select-title")
    public CommonResponse selectTitle(@RequestBody MissionTitleDto req, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.selectTitle(req.getTitle(), customUserDetails);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "미션 동기화 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/missions")
    public List<MissionDto> getMissions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return missionService.getMissions(customUserDetails);
    }

    @Operation(summary = "미션 완료 API", description = "칭호 이름을 통해 미션 구분", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/missions/complete")
    public CommonResponse completeMission(@RequestParam String title, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        missionService.completeMission(title, customUserDetails);
        return responseService.getSuccessResponse();
    }

}
