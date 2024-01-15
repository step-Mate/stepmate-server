package server.stepmate.user;

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
import server.stepmate.user.dto.EmailReq;
import server.stepmate.user.dto.SignInReq;
import server.stepmate.user.dto.SignInRes;
import server.stepmate.user.dto.UserAuthDto;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final EmailService emailService;


    @GetMapping("/users/auth")
    public DataResponse<UserAuthDto> getUserAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(userService.getUserAuth(customUserDetails));
    }

    @PostMapping("/sign-in")
    public DataResponse<SignInRes> signIn(@RequestBody @Valid SignInReq req, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.signIn(req));
    }

    @PostMapping("/sign-up")
    public DataResponse<UserAuthDto> signUp(@RequestBody @Valid UserAuthDto userAuthDto, Errors errors) {
        if (errors.hasErrors()){ ValidationExceptionProvider.throwValidError(errors);}
        return responseService.getDataResponse(userService.signUp(userAuthDto));
    }

    @GetMapping("/email/verification-request")
    public CommonResponse requestVerification(@RequestBody @Valid EmailReq req, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.sendCodeToEmail(req.getEmail());
        return responseService.getSuccessResponse();
    }

}