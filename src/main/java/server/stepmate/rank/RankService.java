package server.stepmate.rank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        rankRepository.saveAll(userRanks);
    }

}
