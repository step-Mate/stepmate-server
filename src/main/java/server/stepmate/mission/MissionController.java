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
    public DataResponse<List<MissionTitleDto>> getUserTitles(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(missionService.getUserTitle(customUserDetails));
    }

    @Operation(summary = "칭호 선택 API", security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/select-title")
    public CommonResponse selectTitle(@RequestBody MissionTitleDto req, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.selectTitle(req.getTitle(), customUserDetails);
        return responseService.getSuccessResponse();
    }


}
