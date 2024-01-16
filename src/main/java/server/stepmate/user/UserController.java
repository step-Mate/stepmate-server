package server.stepmate.user;

import io.swagger.v3.oas.annotations.Operation;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final EmailService emailService;

    @Operation(summary = "현재 인증된 회원 정보 요청 API", description = "JWT 토큰을 기준으로 인증된 회원정보 반환")
    @GetMapping("/users/auth")
    public DataResponse<UserAuthDto> getUserAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(userService.getUserAuth(customUserDetails));
    }

    @Operation(summary = "아이디 Validation API", description = "아이디의 형식, 중복 Validation API")
    @PostMapping("/users/id/validation")
    public CommonResponse getUserIdValidation(@RequestBody @Valid UserIdReq dto, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.checkDuplicatedUserId(dto.getUserId());
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "로그인 인증 API", description = "형식에 맞는 DTO로 요청 -> JWT 토큰을 포함한 회원 정보 반환")
    @PostMapping("/sign-in")
    public DataResponse<SignInRes> signIn(@RequestBody @Valid SignInReq req, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.signIn(req));
    }
    @Operation(summary = "회원가입 API", description = "형식에 맞는 DTO로 요청 -> 회원가입")
    @PostMapping("/sign-up")
    public DataResponse<UserAuthDto> signUp(@RequestBody @Valid UserAuthDto userAuthDto, Errors errors) {
        if (errors.hasErrors()){ ValidationExceptionProvider.throwValidError(errors);}
        return responseService.getDataResponse(userService.signUp(userAuthDto));
    }

    @Operation(summary = "이메일 인증 요청 API", description = "입력한 이메일로 인증코드 발송")
    @GetMapping("/email/verification-request")
    public CommonResponse requestVerification(@RequestBody @Valid EmailReq req, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.sendCodeToEmail(req.getEmail());
        return responseService.getSuccessResponse();
    }

}