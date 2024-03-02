package server.stepmate.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.user.dto.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;


    @Operation(summary = "일일 걸음 저장 API", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/users/save-step")
    public CommonResponse saveStep(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "steps") Integer steps,
            @RequestParam(value = "calories") Integer calories
            ) {
        userService.saveStep(customUserDetails, steps, calories);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "유저 조회 API",description = "missions는 완료한 미션 5개입니다.", security = @SecurityRequirement(name = "JWT"), responses = {
            @ApiResponse(responseCode = "200", description = "요청에 성공", content = @Content(schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "유효한 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/users/{nickname}")
    public UserInfoDto retrieveUserInfo(@PathVariable("nickname") String nickname) {
        return userService.retrieveUserInfo(nickname);
    }

    @Operation(summary = "유저 친구 요청 API", security = @SecurityRequirement(name = "JWT"), responses = {
            @ApiResponse(responseCode = "200", description = "요청에 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효한 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "408", description = "중복 요청 입니다.", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "460", description = "이미 존재하는 친구 입니다.", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/users/{nickname}/friends")
    public CommonResponse friendRequest(@PathVariable("nickname") String nickname, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.friendRequest(nickname, customUserDetails);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "받은 친구 요청 조회 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/users/friend-request")
    public List<FriendRequestDto> getFriendRequests(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getFriendRequests(customUserDetails);
    }

    @Operation(summary = "친구 요청 수락 API", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/users/friend-request/{nickname}")
    public CommonResponse acceptFriendRequest(@PathVariable String nickname, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.acceptFriendRequest(nickname,customUserDetails);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "친구 요청 거절 API", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/users/friend-request/{nickname}/denied")
    public CommonResponse deniedFriendRequest(@PathVariable String nickname, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deniedFriendRequest(nickname, customUserDetails);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "유저 친구 목록 조회 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/users/friends")
    public List<FriendDto> getFriendList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getFriendList(customUserDetails);
    }

    @Operation(summary = "유저 신체정보 변경 API",security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/users/body-info")
    public CommonResponse changeBodyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UserBodyInfoDto dto) {
        userService.changeBodyInfo(customUserDetails,dto);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "유저 내 정보 조회",security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/users/my-info")
    public UserMyInfoDto getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getMyInfo(customUserDetails);
    }

}