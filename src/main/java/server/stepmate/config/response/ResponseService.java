package server.stepmate.config.response;

import org.springframework.stereotype.Service;
import server.stepmate.config.response.exception.CustomExceptionStatus;

@Service
public class ResponseService {

    public CommonResponse getSuccessResponse() {
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(true);
        response.setCode(200);
        response.setMessage("요청에 성공했습니다.");
        return response;
    }

    public <T> DataResponse<T> getDataResponse(T data) {
        DataResponse<T> response = new DataResponse<>();
        response.setResult(data);
        response.setIsSuccess(true);
        response.setCode(200);
        response.setMessage("요청에 성공했습니다.");
        return response;
    }

    public CommonResponse getExceptionResponse(CustomExceptionStatus status) {
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(status.isSuccess());
        response.setCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }
}
