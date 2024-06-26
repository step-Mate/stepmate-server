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
import server.stepmate.rank.RankService;
import server.stepmate.user.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserAccountController {

    private final UserService userService;
    private final ResponseService responseService;
    private final RankService rankService;

    /*@Operation(summary = "현재 인증된 회원 정보 요청 API", description = "JWT 토큰을 기준으로 인증된 회원정보 반환")
    @GetMapping("/users/auth")
    public DataResponse<UserAuthDto> getUserAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(userService.getUserAuth(customUserDetails));
    }*/

    @Operation(summary = "아이디 Validation API", description = "아이디 검증 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "410", description = "아이디 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":410, \"message\":\"아이디를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "412", description = "아이디 중복 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":412, \"message\":\"중복된 아이디 입니다.\"}"))
                    })
    })
    @PostMapping("/users/id/validation")
    public CommonResponse getUserIdValidation(@RequestBody @Valid UserIdReq dto, Errors errors) {

        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.checkDuplicatedUserId(dto.getUserId());
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "닉네임 Validation API")
    @PostMapping("/users/nickname/validation")
    public CommonResponse getNickNameValidation(@RequestBody @Valid NickNameReq dto, Errors errors) {

        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.checkDuplicatedNickname(dto.getNickname());
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "로그인 인증 API", description = "형식에 맞는 DTO로 요청 -> accessToken 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\",\"result\":{\"jwt\":\"string\"}}"))
                    }),
            @ApiResponse(responseCode = "405", description = "로그인 실패",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":405, \"message\":\"아이디 또는 비밀번호가 올바르지 않습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "410", description = "아이디 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":410, \"message\":\"아이디를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "420", description = "비밀번호 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":420, \"message\":\"비밀번호를 입력해 주세요.\"}"))
                    })
    })
    @PostMapping("/sign-in")
    public DataResponse<TokenDto> signIn(@RequestBody @Valid SignInDto dto, Errors errors) {
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.signIn(dto));
    }

    @Operation(summary = "회원가입 API", description = "형식에 맞는 DTO로 요청 -> 회원가입 성공시 accessToken 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\",\"result\":{\"accessToken\":\"string\":\"string\"}}"))
                    }),
            @ApiResponse(responseCode = "410", description = "아이디 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":410, \"message\":\"아이디를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "411", description = "아이디 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":410, \"message\":\"아이디 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "420", description = "비밀번호 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":420, \"message\":\"비밀번호를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "421", description = "비밀번호 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":421, \"message\":\"비밀번호 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "430", description = "닉네임 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":430, \"message\":\"닉네임을 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "431", description = "닉네임 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":431, \"message\":\"닉네임 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "441", description = "이메일 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":441, \"message\":\"이메일 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "450", description = "나이 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":450, \"message\":\"나이를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "451", description = "키 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":451, \"message\":\"키를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "452", description = "몸무게 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":452, \"message\":\"몸무게를 입력해 주세요.\"}"))
                    })
    })
    @PostMapping("/sign-up")
    public DataResponse<TokenDto> signUp(@RequestBody @Valid UserAuthDto userAuthDto, Errors errors) {
        if (errors.hasErrors()){ ValidationExceptionProvider.throwValidError(errors);}
        TokenDto data = userService.signUp(userAuthDto);
        rankService.createNewUserRank(userAuthDto.getNickname());
        return responseService.getDataResponse(data);
    }

    @Operation(summary = "이메일 인증 요청 API", description = "입력한 이메일로 인증코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "403", description = "유효하지않은 인증 코드입니다.",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":403, \"message\":\"이메일 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "441", description = "이메일 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":441, \"message\":\"이메일 형식을 확인해주세요.\"}"))
                    })
    })
    @GetMapping("/email/verification-request")
    public CommonResponse requestVerification(@RequestParam("email") String email) {
//        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.sendCodeToEmail(email);
        return responseService.getSuccessResponse();
    }


    @Operation(summary = "회원가입 이메일 인증 확인 API", description = "이메일에 발송된 인증코드 검증 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "403", description = "유효하지않은 인증 코드 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":403, \"message\":\"유요하지않은 인증 코드 입니다.\"}"))
                    }),
            @ApiResponse(responseCode = "441", description = "이메일 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":441, \"message\":\"이메일 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "442", description = "이메일 중복 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":442, \"message\":\"중복된 이메일입니다.\"}"))
                    })
    })
    @GetMapping("/email/verifications")
    public CommonResponse verificationEmail(@RequestParam("email") String email,
                                            @RequestParam("authCode") String authCode) {
        userService.checkDuplicatedEmail(email);
        userService.verifiedCode(email, authCode);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "이메일 인증으로 아이디 찾는 API", description = "이메일 인증을 통해 유저 아이디를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\",\"result\":{\"userId\":\"string\"}}"))
                    }),
            @ApiResponse(responseCode = "403", description = "유효하지않은 인증 코드 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":403, \"message\":\"유요하지않은 인증 코드 입니다.\"}"))
                    }),
            @ApiResponse(responseCode = "404", description = "이메일을 통해 찾으려는 아이디 없을 때",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":404, \"message\":\"유효한 사용자가 없습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "441", description = "이메일 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":441, \"message\":\"이메일 형식을 확인해주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "442", description = "이메일 중복 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":442, \"message\":\"중복된 이메일입니다.\"}"))
                    })
    })
    @GetMapping("/users/findId")
    public DataResponse<UserIdRes> findIdByEmail(@RequestParam("email") String email,
                                                 @RequestParam("authCode") String authCode) {
//        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.verifiedCode(email, authCode);
        return responseService.getDataResponse(userService.findByEmail(email));
    }

    @Operation(summary = "유저 아이디 이메일 인증을 통해 비밀번호 찾는 API")
    @GetMapping("/users/findPwd")
    public CommonResponse findPwdByIdAndEmail(@RequestParam("userId") String userId,
                                              @RequestParam("email") String email,
                                              @RequestParam("authCode") String authCode) {
        userService.verifiedCode(email, authCode);
        userService.existUser(userId);
        return responseService.getSuccessResponse();
    }


    @Operation(summary = "비밀번호 변경 API", description = "유저 아이디를 기준으로 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":200, \"message\":\"요청에 성공하였습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "404", description = "비밀번호를 변경할 아이디가 존재하지 않는 경우",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":404, \"message\":\"유효한 사용자가 없습니다.\"}"))
                    }),
            @ApiResponse(responseCode = "410", description = "아이디 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":410, \"message\":\"아이디를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "420", description = "비밀번호 공백 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":420, \"message\":\"비밀번호를 입력해 주세요.\"}"))
                    }),
            @ApiResponse(responseCode = "421", description = "비밀번호 형식 오류",
                    content = {@Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"code\":421, \"message\":\"비밀번호 형식을 확인해주세요.\"}"))
                    })

    })
    @PatchMapping("/users/reset-password")
    public CommonResponse resetPassword(@RequestBody @Valid ChangePwdReq req, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.changePassword(req);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "액세스 토큰 재발급 API")
    @GetMapping("/reissue")
    public DataResponse<AccessTokenDto> reissueToken(@RequestHeader("Authorization") String refreshToken) {
        return responseService.getDataResponse(userService.reissueAccessToken(refreshToken));
    }

    @Operation(summary = "유저 회원 탈퇴 API", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/users/withdraw")
    public CommonResponse withdrawUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ValidatePwdDto dto) {
        userService.withdrawUser(customUserDetails, dto);
        rankService.withdrawUserUpdateRank();
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "유저 닉네임 변경 API",description = "닉네임 검증 API로 검증 후 유저 닉네임 변경 API 사용",security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/users/nickname")
    public CommonResponse changeNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("nickname") String nickname) {
        userService.changeNickname(customUserDetails,nickname);
        return responseService.getSuccessResponse();
    }
}