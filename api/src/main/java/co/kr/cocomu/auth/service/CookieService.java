package co.kr.cocomu.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
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

    public void setRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        final Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(isHttpsEnvironment());
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpiredAt);
        response.addCookie(cookie);
    }

    private boolean isHttpsEnvironment() {
        final List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        return profiles.contains("prod") || profiles.contains("dev");
    }

}