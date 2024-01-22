package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.redis.RedisService;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.jwt.JwtTokenProvider;
import server.stepmate.email.EmailService;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final RedisService redisService;

    @Transactional
    public AccessTokenDto signIn(SignInReq req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        AccessTokenDto dto = new AccessTokenDto(jwtTokenProvider.createToken(user.getUserId(), user.getRole()));
        return dto;
    }

    @Transactional
    public AccessTokenDto signUp(UserAuthDto userAuthDto) {
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
        User save = userRepository.save(user);
        AccessTokenDto dto = new AccessTokenDto(jwtTokenProvider.createToken(userAuthDto.getUserId(), user.getRole()));
        return dto;
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

    @Transactional
    public void changePassword(ChangePwdReq dto) {
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_NOT_VALID));
        user.changePassword(passwordEncoder.encode(dto.getPassword()));
    }

    public List<UserRankDto> getUserRanks() {
        List<UserRankDto> UserRankDtoList = new ArrayList<>();
        List<User> userList = userRepository.findTop100ByMonthStep();
        return getUserRankDtoList(userList);
    }

    private List<UserRankDto> getUserRankDtoList(List<User> userList) {
        return userList.stream()
                .map(User::getUserRankDto)
                .collect(Collectors.toList());
    }


}
