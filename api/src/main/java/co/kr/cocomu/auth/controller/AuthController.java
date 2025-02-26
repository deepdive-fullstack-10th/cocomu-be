package co.kr.cocomu.auth.controller;

import co.kr.cocomu.auth.controller.docs.AuthControllerDocs;
import co.kr.cocomu.auth.dto.code.AuthCode;
import co.kr.cocomu.auth.dto.response.AuthResponse;
import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.auth.service.CookieService;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final CookieService cookieService;
    private final JwtProvider jwtProvider;

    @Profile(value = "!prod")
    @GetMapping("/backdoor")
    public Api<AuthResponse> login(final HttpServletResponse response) {
        validateDevEnvironment();
        final String accessToken = jwtProvider.issueAccessToken(1L);
        final String refreshToken = jwtProvider.issueRefreshToken(1L);

        cookieService.setRefreshTokenCookie(response, refreshToken);

        return Api.of(AuthCode.LOGIN_SUCCESS, new AuthResponse(accessToken));
    }

    private void validateDevEnvironment() {
        if (cookieService.isProdEnvironment()) {
            throw new BadRequestException(AuthExceptionCode.INVALID_ACCESS);
        }
    }

}
