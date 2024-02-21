package server.stepmate.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.response.exception.ValidationExceptionProvider;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.email.EmailService;
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
    public CommonResponse saveStep(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(value = "steps") int steps) {
        userService.saveStep(customUserDetails, steps);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "유저 조회 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/users/{nickname}")
    public DataResponse<UserInfoDto> retrieveUserInfo(@PathVariable("nickname") String nickname) {
        return responseService.getDataResponse(userService.retrieveUserInfo(nickname));
    }

    @GetMapping("/users/{nickname}/friends")
    public CommonResponse addFriend(@PathVariable("nickname") String nickname, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.addFriend(nickname, customUserDetails);
        return responseService.getSuccessResponse();
    }

    @GetMapping("/users/friends")
    public DataResponse<List<FriendDto>> getFriendList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(userService.getFriendList(customUserDetails));
    }


    //미완성
    /*@GetMapping("/myinfo")
    public DataResponse getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse()
    }*/

}