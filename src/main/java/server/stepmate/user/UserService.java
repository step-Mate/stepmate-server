package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.authentication.CustomUserDetails;
import server.stepmate.config.security.jwt.JwtTokenProvider;
import server.stepmate.email.EmailService;
import server.stepmate.user.dto.SignInReq;
import server.stepmate.user.dto.SignInRes;
import server.stepmate.user.dto.UserAuthDto;
import server.stepmate.user.entity.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Transactional
    public SignInRes signIn(SignInReq req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        SignInRes res = SignInRes.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .jwt(jwtTokenProvider.createToken(user.getUserId(), user.getRole()))
                .build();

        return res;
    }

    @Transactional
    public UserAuthDto signUp(UserAuthDto userAuthDto) {
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
        userAuthDto.setId(save.getId());
        userAuthDto.setJwt(jwtTokenProvider.createToken(userAuthDto.getUserId(), user.getRole()));
        return userAuthDto;
    }

    public UserAuthDto getUserAuth(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        UserAuthDto userAuthDto = user.getUserAuthDto();
        userAuthDto.setJwt(jwtTokenProvider.createToken(user.getUserId(), user.getRole()));
        return userAuthDto;
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "Step-Mate 이메일 인증 번호";
        String authCode = this.createCode();
        emailService.sendEmail(toEmail,title,authCode);
    }

    private void checkDuplicatedEmail(String email) {
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

}
