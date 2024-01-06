package server.stepmate.config.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {

    /**
     * 400 : Request 오류
     */
    REQUEST_ERROR(false, 400, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 401, "JWT이 비어있습니다."),
    INVALID_JWT(false, 402, "유효하지 않은 JWT입니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;
}
