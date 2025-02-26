package co.kr.cocomu.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private final Environment environment;
    private final int refreshTokenExpiredAt;

    public CookieService(
        final Environment environment,
        @Value("${jwt.refresh-exp}") final int refreshTokenExpiredAt
    ) {
        this.environment = environment;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }

    public void setRefreshTokenCookie(final HttpServletResponse response, String refreshToken) {
        final Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(isProdEnvironment());
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpiredAt);
        response.addCookie(cookie);
    }

    public boolean isProdEnvironment() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

}