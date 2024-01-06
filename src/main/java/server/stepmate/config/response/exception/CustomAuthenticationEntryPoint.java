package server.stepmate.config.response.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))+" : Exception transmitted to AuthenticationEntryPoint");

        String exception = (String) request.getAttribute("exception");
        if (exception == null || exception.equals(CustomExceptionStatus.EMPTY_JWT.getMessage())) {
            response.sendRedirect("/errors/empty-jwt");
        } else if (exception.equals(CustomExceptionStatus.INVALID_JWT.getMessage())) {
            response.sendRedirect("/errors/invalid-jwt");
        }

    }
}
