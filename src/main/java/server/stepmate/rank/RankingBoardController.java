package server.stepmate.rank;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.rank.dto.UserRankDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingBoardController {

    private final ResponseService responseService;
    private final RankService rankService;

    @GetMapping("/rank-board")
    public DataResponse<List<UserRankDto>> getRankBoard(@RequestParam("page") Integer page) {
        return responseService.getDataResponse(rankService.getUserRank(page));
    }

    @GetMapping("/test")
    public CommonResponse test() {
        rankService.updateRank();
        return responseService.getSuccessResponse();
    }
}
