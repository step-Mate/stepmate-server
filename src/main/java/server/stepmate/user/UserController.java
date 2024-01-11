package server.stepmate.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.response.exception.ValidationExceptionProvider;
import server.stepmate.user.dto.SignInReq;
import server.stepmate.user.dto.SignInRes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;


    @PostMapping("/sign-in")
    public DataResponse<SignInRes> signIn(@RequestBody @Valid SignInReq req, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.signIn(req));
    }
}