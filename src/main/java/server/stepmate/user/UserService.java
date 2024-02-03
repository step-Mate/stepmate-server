package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import server.stepmate.mission.entity.Mission;
import server.stepmate.mission.entity.UserMission;
import server.stepmate.user.dto.*;
import server.stepmate.user.entity.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final RedisService redisService;

    @Transactional
    public TokenDto signIn(SignInReq req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        redisService.setValues(user.getUserId(), refreshToken,Duration.ofMillis(JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME));

        TokenDto dto = new TokenDto(refreshToken, accessToken);
        return dto;
    }

    @Transactional
    public TokenDto signUp(UserAuthDto userAuthDto) {
        if (userRepository.findByUserId(userAuthDto.getUserId()).isPresent()) {
            throw new CustomException(CustomExceptionStatus.USER_EXISTS_ID);
        }
        if (userAuthDto.getNickname() != null) {
            if (userRepository.findByNickname(userAuthDto.getNickname()).isPresent()) {
                throw new CustomException(CustomExceptionStatus.USER_EXISTS_NICKNAME);
            }
        }

        userAuthDto.setPassword(passwordEncoder.encode(userAuthDto.getPassword()));
        User user = User.createUser(userAuthDto);
        User save = userRepository.save(createUserMissions(user));

        String accessToken = jwtTokenProvider.createAccessToken(userAuthDto.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(userAuthDto.getUserId());

        redisService.setValues(userAuthDto.getUserId(),refreshToken,Duration.ofMillis(JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME));

        TokenDto dto = new TokenDto(refreshToken, accessToken);
        return dto;
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

//    public List<UserRankDto> getUserRanks() {
//        List<UserRankDto> UserRankDtoList = new ArrayList<>();
//        List<User> userList = userRepository.findTop100ByMonthStep();
//        return getUserRankDtoList(userList);
//    }

    private List<UserRankDto> getUserRankDtoList(List<User> userList) {
        return userList.stream()
                .map(User::getUserRankDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void selectTitle(String title, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        user.changeTitle(title);
        userRepository.save(user);
    }

    @Transactional
    public void resetAllUserMonthStep() {
        userRepository.resetAllUserMonthStep();
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

    public List<UserRankDto> getUserRanks(int page) {
        PageRequest pageRequest = PageRequest.of(page, 25);
        Page<User> userPage = userRepository.findAllOrderByMonthStepLevelNickname(pageRequest);
        List<User> userList = userPage.getContent();

        List<UserRankDto> userRankDtoList = getRankDtoList(userList);

        assignRanks(userRankDtoList);

        return userRankDtoList;
    }

    private  List<UserRankDto> getRankDtoList(List<User> userList) {
        return userList.stream().map(User::getUserRankDto).toList();
    }

    private void assignRanks(List<UserRankDto> userRankDtoList) {
        int Rank = 1;
        int currentRank = 1;
        int previousMonthStep = Integer.MAX_VALUE;

        for (UserRankDto userRankDto : userRankDtoList) {
            if (userRankDto.getMonthStep() == previousMonthStep) {
                userRankDto.setRank(currentRank);
            } else {
                userRankDto.setRank(Rank);
                currentRank = Rank;
            }

            previousMonthStep = userRankDto.getMonthStep();
            Rank++;
        }

    }
}
