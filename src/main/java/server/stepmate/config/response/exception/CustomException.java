package server.stepmate.config.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException {
    CustomExceptionStatus customExceptionStatus;
}
