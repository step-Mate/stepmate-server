package server.stepmate.rankingboard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.user.UserService;
import server.stepmate.user.dto.UserRankDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingBoardController {

    private ResponseService responseService;
    private UserService userService;

    @GetMapping("/rank-board")
    public DataResponse<List<UserRankDto>> getRankBoard() {
        return responseService.getDataResponse(userService.getUserRanks());
    }
}