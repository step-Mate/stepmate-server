package server.stepmate.config.response.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.ResponseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponse> customException(CustomException customException) {
        CustomExceptionStatus status = customException.getCustomExceptionStatus();
        log.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " + status.getMessage());
        return ResponseEntity.status(status.getCode()).body(responseService.getExceptionResponse(status));
    }
}
