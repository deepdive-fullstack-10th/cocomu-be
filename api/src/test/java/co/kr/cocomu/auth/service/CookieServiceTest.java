package co.kr.cocomu.auth.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock
    private Environment environment;

    @Mock
    private HttpServletResponse response;

    private CookieService cookieService;

    private static final int REFRESH_TOKEN_MAX_AGE = 86400;

    @BeforeEach
    void setUp() {
        cookieService = new CookieService(environment, REFRESH_TOKEN_MAX_AGE);
    }

    @Test
    void 로컬_환경에서_쿠키를_설정한다() {
        // given
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        String refreshToken = "refresh-token-value";
        when(environment.getActiveProfiles()).thenReturn(new String[]{"default"});

        // when
        cookieService.setRefreshTokenCookie(response, refreshToken);

        // then
        verify(response).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertThat(capturedCookie.getName()).isEqualTo("refreshToken");
        assertThat(capturedCookie.getValue()).isEqualTo(refreshToken);
        assertThat(capturedCookie.isHttpOnly()).isTrue();
        assertThat(capturedCookie.getSecure()).isFalse();
        assertThat(capturedCookie.getPath()).isEqualTo("/");
        assertThat(capturedCookie.getMaxAge()).isEqualTo(REFRESH_TOKEN_MAX_AGE);
    }

    @Test
    void 개발_환경에서_쿠키를_설정한다() {
        // given
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        String refreshToken = "refresh-token-value";
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // when
        cookieService.setRefreshTokenCookie(response, refreshToken);

        // then
        verify(response).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertThat(capturedCookie.getSecure()).isTrue();
    }

    @Test
    void 프로덕션_환경에서_쿠키를_설정한다() {
        // given
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        String refreshToken = "refresh-token-value";
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        // when
        cookieService.setRefreshTokenCookie(response, refreshToken);

        // then
        verify(response).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertThat(capturedCookie.getSecure()).isTrue();
    }

}