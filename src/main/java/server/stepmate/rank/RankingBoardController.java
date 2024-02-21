package server.stepmate.rank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.config.response.CommonResponse;
import server.stepmate.config.response.DataResponse;
import server.stepmate.config.response.ResponseService;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.rank.dto.FriendRankDto;
import server.stepmate.rank.dto.UserRankDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingBoardController {

    private final ResponseService responseService;
    private final RankService rankService;

    @Operation(summary = "랭킹보드 랭킹 조회 API",
            description = "page : 0번 부터 시작 rankChange : 전일대비 순위 변화률 양수면 순위가 높아지고 음수면 순위가 떨어지는 의미",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/rank-board")
    public DataResponse<List<UserRankDto>> getRankBoard(@RequestParam("page") Integer page) {
        return responseService.getDataResponse(rankService.getUserRank(page));
    }

    @Operation(summary = "랭킹보드 친구 랭킹 조회 API", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/rank-board/friends")
    public DataResponse<List<FriendRankDto>> getFriendRankBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(rankService.getFriendRankList(customUserDetails));
    }
}
