package server.stepmate.rank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.rank.dto.FriendRankDto;
import server.stepmate.rank.dto.UserRankDto;
import server.stepmate.rank.entity.Rank;
import server.stepmate.user.FriendshipRepository;
import server.stepmate.user.UserService;
import server.stepmate.user.entity.Friendship;
import server.stepmate.user.entity.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RankService {

    private final UserService userService;
    private final RankRepository rankRepository;
    private final FriendshipRepository friendshipRepository;

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

    public List<FriendRankDto> getFriendRankList(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<Friendship> friendships = friendshipRepository.findAllByUser(user);
        List<FriendRankDto> friendRankDtoList = new ArrayList<>();
        for (Friendship friendship : friendships) {
            User friend = friendship.getFriend();

            FriendRankDto friendRankDto = FriendRankDto.builder()
                    .level(friend.getLevel())
                    .nickname(friend.getNickname())
                    .monthStep(friend.getMonthStep())
                    .title(friend.getTitle())
                    .build();

            friendRankDtoList.add(friendRankDto);
        }

        FriendRankDto friendRankDto = FriendRankDto.builder()
                .level(user.getLevel())
                .nickname(user.getNickname())
                .monthStep(user.getMonthStep())
                .title(user.getTitle())
                .build();

        friendRankDtoList.add(friendRankDto);

        friendRankDtoList.sort(Comparator.comparingInt(FriendRankDto::getMonthStep)
                .reversed()
                .thenComparingInt(FriendRankDto::getLevel)
                .thenComparing(FriendRankDto::getNickname));

        assignRanks(friendRankDtoList);

        return friendRankDtoList;
    }

    private void assignRanks(List<FriendRankDto> friendRankDtoList) {
        int Rank = 1;
        int currentRank = 1;
        int previousMonthStep = Integer.MAX_VALUE;

        for (FriendRankDto friendRankDto : friendRankDtoList) {
            if (friendRankDto.getMonthStep() == previousMonthStep) {
                friendRankDto.setRanking(currentRank);
            } else {
                friendRankDto.setRanking(Rank);
                currentRank = Rank;
            }

            previousMonthStep = friendRankDto.getMonthStep();
            Rank++;
        }

    }

    public UserRankDto getMyInfoRank(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        Rank rank = rankRepository.findByNickname(user.getNickname()).orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_NOT_VALID));
        return rank.getUserRankDto();
    }
}
