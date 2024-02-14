package server.stepmate.rank;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.user.UserService;
import server.stepmate.user.dto.UserRankDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingBoardController {

    private final ResponseService responseService;
    private final RankService rankService;

    /*@GetMapping("/rank-board")
    public DataResponse<List<UserRankDto>> getRankBoard(@RequestParam("page") int page) {
        return responseService.getDataResponse(userService.getUserRanks(page));
    }*/

    @GetMapping("/test")
    public CommonResponse test() {
        rankService.updateRank();
        return responseService.getSuccessResponse();
    }
}
