package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;
import server.stepmate.config.security.jwt.JwtTokenProvider;
import server.stepmate.user.dto.SignInReq;
import server.stepmate.user.dto.SignInRes;
import server.stepmate.user.entity.User;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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
}
