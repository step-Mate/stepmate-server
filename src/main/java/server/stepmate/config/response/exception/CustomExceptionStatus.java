package server.stepmate.config.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {


    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 400 : Request 오류
     */
    REQUEST_ERROR(false, 400, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 401, "JWT이 비어있습니다."),
    INVALID_JWT(false, 402, "유효하지 않은 JWT입니다."),
    INVALID_AUTH_CODE(false, 403, "유요하지 않은 인증코드입니다."),
    USER_NOT_VALID(false, 404, "유효한 사용자가 없습니다."),
    FAILED_TO_LOGIN(false, 405, "아이디 또는 비밀번호가 올바르지 않습니다."),

    ACCOUNT_ACCESS_DENIED(false,406,"권한이 없습니다."),

    NOT_AUTHENTICATED_ACCOUNT(false,407,"로그인이 필요합니다."),

    DUPLICATE_REQUEST(false,408,"중복 요청 입니다."),

    USER_EMPTY_ID(false,410,"아이디를 입력해 주세요"),
    USER_INVALID_ID(false, 411, "아이디 형식을 확인해 주세요"),
    USER_EXISTS_ID(false,412,"중복된 아이디 입니다."),

    USER_EMPTY_PASSWORD(false,420,"비밀번호를 입력해 주세요"),
    USER_INVALID_PASSWORD(false, 421, "비밀번호 형식을 확인해 주세요"),

    USER_EMPTY_NICKNAME(false,430,"닉네임을 입력해 주세요"),
    USER_INVALID_NICKNAME(false, 431, "닉네임 형식을 확인해 주세요"),
    USER_EXISTS_NICKNAME(false,432,"중복된 닉네임 입니다."),

    USER_EMPTY_EMAIL(false,440,"이메일을 입력해 주세요"),
    USER_INVALID_EMAIL(false, 441, "이메일 형식을 확인해 주세요"),
    USER_EXISTS_EMAIL(false,442,"중복된 이메일 입니다."),

    USER_EMPTY_AGE(false,450,"나이를 입력해 주세요"),
    USER_EMPTY_HEIGHT(false,451,"키를 입력해 주세요"),
    USER_EMPTY_WEIGHT(false,452,"몸무게를 입력해 주세요"),

    USER_ALREADY_EXISTS_FRIEND(false, 460,"이미 존재하는 친구 입니다."),


    /**
     * 500 : 서버 내부 오류
     */
    RESPONSE_ERROR(false, 500, "값을 불러오는데 실패하였습니다."),
    NO_SUCH_ALGORITHM(false, 555, "난수 생성 알고리즘을 찾지 못했습니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;
}
