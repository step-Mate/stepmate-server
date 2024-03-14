package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.redis.RedisService;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.config.security.jwt.JwtTokenProvider;
import server.stepmate.email.EmailService;
import server.stepmate.mission.MissionRepository;
import server.stepmate.mission.MissionService;
import server.stepmate.mission.UserMissionRepository;
import server.stepmate.mission.dto.MissionProgressDto;
import server.stepmate.mission.entity.Mission;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.rank.RankRepository;
import server.stepmate.rank.entity.Rank;
import server.stepmate.user.dto.*;
import server.stepmate.user.entity.DailyStep;
import server.stepmate.user.entity.FriendRequest;
import server.stepmate.user.entity.Friendship;
import server.stepmate.user.entity.User;
import server.stepmate.user.entity.enumtypes.RequestStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final DailyStepRepository dailyStepRepository;
    private final RankRepository rankRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final RedisService redisService;
    private final MissionService missionService;
    private final TextEncryptor textEncryptor;

    @Transactional
    public TokenDto signIn(SignInReq req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        redisSaveRefreshToken(user, refreshToken);

        return new TokenDto(refreshToken, accessToken);
    }

    private void redisSaveRefreshToken(User user, String refreshToken) {
        Duration refreshTokenExpireTime = Duration.ofMillis(JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME);
        redisService.setValues(user.getUserId(), refreshToken, refreshTokenExpireTime);
    }

    @Transactional
    public TokenDto signUp(UserAuthDto userAuthDto) {
        checkDuplicatedUserId(userAuthDto.getUserId());
        checkDuplicatedNickname(userAuthDto.getNickname());

        userAuthDto.setPassword(passwordEncoder.encode(userAuthDto.getPassword()));

        BodyInfoEncrytDto bodyInfoEncrytDto = getBodyInfoEncrytDto(userAuthDto);

        User user = User.createUser(userAuthDto, bodyInfoEncrytDto);
        User save = userRepository.save(createUserMissions(user));

        String accessToken = jwtTokenProvider.createAccessToken(userAuthDto.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(userAuthDto.getUserId());

        redisSaveRefreshToken(user, refreshToken);

        return new TokenDto(refreshToken, accessToken);
    }

    private BodyInfoEncrytDto getBodyInfoEncrytDto(UserAuthDto userAuthDto) {
        return BodyInfoEncrytDto.builder()
                .age(textEncryptor.encrypt(String.valueOf(userAuthDto.getAge())))
                .weight(textEncryptor.encrypt(String.valueOf(userAuthDto.getWeight())))
                .height(textEncryptor.encrypt(String.valueOf(userAuthDto.getHeight())))
                .build();
    }

    @Transactional
    public User createUserMissions(User user) {
        List<Mission> allMissions = missionRepository.findAll();
        System.out.println("allMissions = " + allMissions);
        List<UserMission> userMissions = new ArrayList<>();

        for (Mission mission : allMissions) {
            UserMission userMission = UserMission.builder()
                    .user(user)
                    .mission(mission)
                    .isComplete(false)
                    .stepCurrentValue(0)
                    .calorieCurrentValue(0)
                    .build();
            userMissions.add(userMission);
        }
        System.out.println("userMissions = " + userMissions);

        user.initUserMissions(userMissions);
        return user;
    }

    public void sendCodeToEmail(String toEmail) {
        String title = "Step-Mate 이메일 인증 번호";
        String authCode = this.createCode();
        emailService.sendEmail(toEmail,title,authCode);
        redisService.setValues(toEmail,authCode, Duration.ofMillis(300000));//5분
    }
    public void checkDuplicatedUserId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            throw new CustomException(CustomExceptionStatus.USER_EXISTS_ID);
        }
    }

    public void checkDuplicatedNickname(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            throw new CustomException(CustomExceptionStatus.USER_EXISTS_NICKNAME);
        }
    }

    public void checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new CustomException(CustomExceptionStatus.USER_EXISTS_EMAIL);
        }
    }

    private String createCode() {
        int length = 6;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(CustomExceptionStatus.NO_SUCH_ALGORITHM);
        }

    }

    public void verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValues(email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if (!authResult) {
            throw new CustomException(CustomExceptionStatus.INVALID_AUTH_CODE);
        }
    }

    public UserIdRes findByEmail(String email) {
        UserIdRes userIdRes = new UserIdRes();
        Optional<User> optional = userRepository.findByEmail(email);

        if (optional.isEmpty()) {
            throw new CustomException(CustomExceptionStatus.RESPONSE_ERROR);
        }
        User user = optional.get();
        userIdRes.setUserId(user.getUserId());
        return userIdRes;
    }

    public void existUser(String userId) {
        if (!userRepository.existsByUserId(userId)) {
            throw new CustomException(CustomExceptionStatus.USER_NOT_VALID);
        }
    }

    @Transactional
    public void changePassword(ChangePwdReq dto) {
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_NOT_VALID));
        user.changePassword(passwordEncoder.encode(dto.getPassword()));
    }

    @Transactional
    public void withdrawUser(CustomUserDetails customUserDetails, ValidatePwdDto dto) {
        User user = customUserDetails.getUser();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }
        rankRepository.deleteByNickname(user.getNickname());
        userRepository.deleteById(user.getId());
    }

//    public List<UserRankDto> getUserRanks() {
//        List<UserRankDto> UserRankDtoList = new ArrayList<>();
//        List<User> userList = userRepository.findTop100ByMonthStep();
//        return getUserRankDtoList(userList);
//    }

//    private List<UserRankDto> getUserRankDtoList(List<User> userList) {
//        return userList.stream()
//                .map(User::getUserRankDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void selectDesignation(String designation, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        user.changeDesignation(designation);
        userRepository.save(user);
    }

    @Transactional
    public void resetAllUserMonthStep() {
        userRepository.resetAllUserMonthStep();
    }

    @Transactional
    public void resetAllUserTodayStep() {
        userRepository.resetAllUserTodayStep();
    }

    @Transactional
    public void resetAllDailyStep() {
        dailyStepRepository.deleteAll();
    }

    public AccessTokenDto reissueAccessToken(String token) {
        String userId = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_JWT));
        String refreshToken = redisService.getValues(user.getUserId());
        if (!token.equals(refreshToken)) {
            throw new CustomException(CustomExceptionStatus.INVALID_JWT);
        }
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());

        AccessTokenDto accessTokenDto = new AccessTokenDto(accessToken);
        return accessTokenDto;
    }

    public List<Rank> getUserRanks() {

        List<User> userList = userRepository.findAllByOrderByMonthStepDesc();
        List<Rank> userRankList = getRankList(userList);

        assignRanks(userRankList);

        return userRankList;
    }

    private  List<Rank> getRankList(List<User> userList) {
        return userList.stream().map(User::getUserRank).toList();
    }


    public void assignRanks(List<Rank> userRankList) {
        int Rank = 1;
        int currentRank = 1;
        int previousMonthStep = Integer.MAX_VALUE;

        for (Rank rank : userRankList) {
            if (rank.getMonthStep() == previousMonthStep) {
                rank.updateRanking(currentRank);
            } else {
                rank.updateRanking(Rank);
                currentRank = Rank;
            }

            previousMonthStep = rank.getMonthStep();
            Rank++;
        }

    }

    @Transactional
    public void saveStep(CustomUserDetails customUserDetails,Integer steps) {
        LocalDate date = LocalDate.now();
        User user = customUserDetails.getUser();

        double xp = (double) steps / 1000;
        double roundedXp = ((int)(xp*100))/100.0;

        user.updateStep(steps);
        user.updateXp(roundedXp);

        int weight = Integer.parseInt(textEncryptor.decrypt(user.getWeight()));
        double calorie = 3.0 * (3.5 * weight * user.getTotalStep() * 0.0008 * 15) * 5 / 1000;
        double calories = ((int) (calorie * 100)) / 100.0;

        Optional<DailyStep> dailyStepByDate = dailyStepRepository.findDailyStepByDate(user.getId(), date);

        if (dailyStepByDate.isPresent()) {
            DailyStep dailyStep = dailyStepByDate.get();
            dailyStep.addSteps(steps);
        } else {
            DailyStep dailyStep = DailyStep.createDailyStep(steps, user, date);
            dailyStepRepository.save(dailyStep);
        }

        List<UserMission> userMissionList = userMissionRepository.findAllProgressMissionById(user.getId());

        for (UserMission userMission : userMissionList) {
            userMission.addCurrentValue(steps, calories);
        }

        userRepository.save(user);

        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " +user.getNickname() + " step = " + steps);

    }

    @Transactional
    public void saveStepMidnight(CustomUserDetails customUserDetails,Integer steps) {
        LocalDate date = LocalDate.now().minusDays(1);
        User user = customUserDetails.getUser();

        double xp = (double) steps / 1000;
        double roundedXp = ((int)(xp*100))/100.0;

        user.updateStep(steps);
        user.updateXp(roundedXp);

        int weight = Integer.parseInt(textEncryptor.decrypt(user.getWeight()));
        double calorie = 3.0 * (3.5 * weight * user.getTotalStep() * 0.0008 * 15) * 5 / 1000;
        double calories = ((int) (calorie * 100)) / 100.0;

        Optional<DailyStep> dailyStepByDate = dailyStepRepository.findDailyStepByDate(user.getId(), date);

        if (dailyStepByDate.isPresent()) {
            DailyStep dailyStep = dailyStepByDate.get();
            dailyStep.addSteps(steps);
        } else {
            DailyStep dailyStep = DailyStep.createDailyStep(steps, user, date);
            dailyStepRepository.save(dailyStep);
        }

        List<UserMission> userMissionList = userMissionRepository.findAllProgressMissionById(user.getId());

        for (UserMission userMission : userMissionList) {
            userMission.addCurrentValue(steps, calories);
        }

        userRepository.save(user);

        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : (자정) " +user.getNickname() + " step = " + steps);

    }

    public UserInfoDto retrieveUserInfo(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));
        return getUserInfoDto(user);
    }

    private UserInfoDto getUserInfoDto(User user) {
        List<DailyStep> userDailyStep = dailyStepRepository.findUserDailyStep(user.getId());
        List<DailyStepDto> dailyStepDtoList = userDailyStep.stream().map(DailyStep::getDailyStepDto).toList();
        List<UserMission> userMissions = userMissionRepository.findTop5ByUserMission(user.getId());
        List<MissionProgressDto> missionProgressDtoListDtoList = missionService.getMissionProgressDtoList(userMissions);
        Rank rank = rankRepository.findByNickname(user.getNickname()).orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));

        return UserInfoDto.builder()
                .ranking(rank.getRanking())
                .rankChange(rank.getRankChange())
                .nickname(user.getNickname())
                .level(user.getLevel())
                .totalStep(user.getTotalStep())
                .title(user.getTitle())
                .dailySteps(dailyStepDtoList)
                .missions(missionProgressDtoListDtoList)
                .build();
    }

    @Transactional
    public void friendRequest(String nickname, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        User friend = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_NOT_VALID));

        if (user.getNickname().equals(friend.getNickname())) {
            throw new CustomException(CustomExceptionStatus.DUPLICATE_REQUEST);
        }

        if (friendshipRepository.existsByUserAndFriend(user, friend)) {
            throw new CustomException(CustomExceptionStatus.USER_ALREADY_EXISTS_FRIEND);
        }

        if (friendRequestRepository.existsBySenderAndReceiver(user, friend)) {
            throw new CustomException(CustomExceptionStatus.DUPLICATE_REQUEST);
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .receiver(friend)
                .sender(user)
                .status(RequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);

    }

    public List<FriendRequestDto> getFriendRequests(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<FriendRequest> friendRequestList = friendRequestRepository.findFriendRequestByReceiver(user);

        return friendRequestList.stream().map(FriendRequest::getFriendRequestDto).toList();
    }

    public List<FriendDto> getFriendList(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<Friendship> friendships = friendshipRepository.findAllByUser(user.getId());
        List<FriendDto> friendDtoList = new ArrayList<>();
        for (Friendship friendship : friendships) {
            User friend = friendship.getFriend();
            FriendDto friendDto = new FriendDto(friend.getNickname(), friend.getLevel(), friend.getTitle());
            friendDtoList.add(friendDto);
        }
        return friendDtoList;
    }

    @Transactional
    public void acceptFriendRequest(String senderNickname, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        User friend = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));


        Friendship friendship1 = Friendship.builder()
                .user(user)
                .friend(friend)
                .build();

        Friendship friendship2 = Friendship.builder()
                .user(friend)
                .friend(user)
                .build();

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);

        friendRequestRepository.deleteBySenderAndReceiver(friend,user);
    }

    @Transactional
    public void deniedFriendRequest(String senderNickname, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        User friend = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));

        friendRequestRepository.deleteBySenderAndReceiver(friend,user);
    }

    @Transactional
    public void deleteFriend(String friendNickname, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        User friend = userRepository.findByNickname(friendNickname)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));

        friendshipRepository.deleteByUserAndFriend(user,friend);
        friendshipRepository.deleteByUserAndFriend(friend,user);
    }

    @Transactional
    public void changeBodyInfo(CustomUserDetails customUserDetails,UserBodyInfoDto dto) {
        User user = customUserDetails.getUser();
        BodyInfoEncrytDto bodyInfoEncrytDto = BodyInfoEncrytDto.builder()
                .age(textEncryptor.encrypt(String.valueOf(dto.getAge())))
                .weight(textEncryptor.encrypt(String.valueOf(dto.getWeight())))
                .height(textEncryptor.encrypt(String.valueOf(dto.getHeight())))
                .build();
        user.changeBodyInfo(bodyInfoEncrytDto.getAge(), bodyInfoEncrytDto.getHeight(), bodyInfoEncrytDto.getWeight());
        userRepository.save(user);
    }

    @Transactional
    public void changeNickname(CustomUserDetails customUserDetails, String nickname) {
        User user = customUserDetails.getUser();
        Rank rank = rankRepository.findByNickname(user.getNickname()).orElseThrow(() -> new CustomException(CustomExceptionStatus.RESPONSE_ERROR));
        rank.changeNickname(nickname);
        user.changeNickname(nickname);
        rankRepository.save(rank);
        userRepository.save(user);
    }

    public UserMyInfoDto getMyInfo(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return UserMyInfoDto.builder()
                .nickname(user.getNickname())
                .title(user.getTitle())
                .level(user.getLevel())
                .build();

    }
}
