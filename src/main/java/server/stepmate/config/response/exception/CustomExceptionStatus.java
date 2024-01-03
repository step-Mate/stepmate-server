package server.stepmate.config.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {

    /**
     * 400 : Request 오류
     */
    REQUEST_ERROR(false, 400, "입력값을 확인해주세요.");


    private final boolean isSuccess;
    private final int code;
    private final String message;
}
