package server.stepmate.rank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.rank.dto.UserRankDto;
import server.stepmate.rank.entity.Rank;
import server.stepmate.user.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RankService {

    private final UserService userService;
    private final RankRepository rankRepository;

    @Transactional
    public void updateRank() {

        List<Rank> userRanks = userService.getUserRanks();

        for (Rank rank : userRanks) {
            String nickname = rank.getNickname();
            Integer oldRank = rankRepository.findRank(nickname);
            if (oldRank == null) {
                oldRank=rank.getRanking();
            }
            Integer rankNum = oldRank - rank.getRanking();
            rank.calculateRankChange(rankNum);
        }

        rankRepository.deleteAll();

        rankRepository.saveAll(userRanks);
    }

    public List<UserRankDto> getUserRank(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 25);
        Page<Rank> pageRank = rankRepository.findPageRank(pageRequest);
        List<Rank> userRank = pageRank.getContent();

        return getUserRankDtoList(userRank);
    }

    private List<UserRankDto> getUserRankDtoList(List<Rank> userRank) {
        return userRank.stream()
                .map(Rank::getUserRankDto)
                .toList();
    }

}
