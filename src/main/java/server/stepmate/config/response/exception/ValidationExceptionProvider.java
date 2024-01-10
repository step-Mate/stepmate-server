package server.stepmate.config.response.exception;

import org.springframework.validation.Errors;

public class ValidationExceptionProvider {

    public static void throwValidError(Errors errors) {
        String errorCode = errors.getFieldError().getCode();
        String errorTarget = errors.getFieldError().getField();
        throw new CustomException(ValidationExceptionProvider.getExceptionStatus(errorCode, errorTarget));
    }

    public static CustomExceptionStatus getExceptionStatus(String code, String target) {

        if (code.equals("NotBlank")) {
            if(target.equals("userId")) return CustomExceptionStatus.USER_EMPTY_ID;
            else if (target.equals("password")) return CustomExceptionStatus.USER_EMPTY_PASSWORD;
            else if (target.equals("nickname")) return CustomExceptionStatus.USER_EMPTY_NICKNAME;
            else if (target.equals("email")) return CustomExceptionStatus.USER_EMPTY_EMAIL;
            else if (target.equals("age")) return CustomExceptionStatus.USER_EMPTY_AGE;
            else if (target.equals("height")) return CustomExceptionStatus.USER_EMPTY_HEIGHT;
            else if (target.equals("weight")) return CustomExceptionStatus.USER_EMPTY_WEIGHT;
        } else if (code.equals("Pattern")) {
            if(target.equals("userId")) return CustomExceptionStatus.USER_INVALID_ID;
            else if (target.equals("password")) return CustomExceptionStatus.USER_INVALID_PASSWORD;
            else if (target.equals("nickname")) return CustomExceptionStatus.USER_INVALID_NICKNAME;
            else if (target.equals("email")) return CustomExceptionStatus.USER_INVALID_EMAIL;
        } else if (code.equals("Email")) {
            return CustomExceptionStatus.USER_INVALID_EMAIL;
        }
        return CustomExceptionStatus.RESPONSE_ERROR;
    }
}
