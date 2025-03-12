package co.kr.cocomu.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.auth.domain.OAuth2Provider;
import co.kr.cocomu.auth.controller.code.AuthApiCode;
import co.kr.cocomu.auth.dto.request.OAuthRequest;
import co.kr.cocomu.auth.dto.response.AuthResponse;
import co.kr.cocomu.auth.service.CookieService;
import co.kr.cocomu.auth.service.GithubService;
import co.kr.cocomu.auth.service.GoogleService;
import co.kr.cocomu.auth.service.KakaoService;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.jwt.JwtProvider;
import co.kr.cocomu.common.security.SecurityConfig;
import co.kr.cocomu.common.template.GetRequestTemplate;
import co.kr.cocomu.common.template.PostRequestTemplate;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(AuthController.class)
@Import(value = {SecurityConfig.class})
class AuthExecutorControllerTest {

    private static final String PATH_PREFIX = "/api/v1/auth";

    @MockBean private JwtProvider jwtProvider;
    @MockBean private CookieService cookieService;
    @MockBean private GithubService githubService;
    @MockBean private KakaoService kakaoService;
    @MockBean private GoogleService googleService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void 백도어_로그인이_성공한다() {
        // given
        String path = PATH_PREFIX + "/backdoor";
        String backdoorAccessToken = "backdoor-access-token";
        String backdoorRefreshToken = "backdoor-refresh-token";

        when(jwtProvider.issueAccessToken(1L)).thenReturn(backdoorAccessToken);
        when(jwtProvider.issueRefreshToken(1L)).thenReturn(backdoorRefreshToken);
        doNothing().when(cookieService).setRefreshTokenCookie(any(), eq(backdoorRefreshToken));

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        Api<AuthResponse> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo(backdoorAccessToken);
        verify(cookieService).setRefreshTokenCookie(any(), eq(backdoorRefreshToken));
    }

    @ParameterizedTest
    @EnumSource(value = OAuth2Provider.class)
    void OAuth2_로그인이_성공한다(OAuth2Provider provider) {
        // given
        String path = PATH_PREFIX + "/oauth-login";
        OAuthRequest request = new OAuthRequest(provider, "mock-code");
        Long mockUserId = 123L;
        String mockAccessToken = "mock-access-token";
        String mockRefreshToken = "mock-refresh-token";

        setLoginConfigByProvider(request, mockUserId);
        when(jwtProvider.issueAccessToken(mockUserId)).thenReturn(mockAccessToken);
        when(jwtProvider.issueRefreshToken(mockUserId)).thenReturn(mockRefreshToken);
        doNothing().when(cookieService).setRefreshTokenCookie(any(), eq(mockRefreshToken));

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, request);

        // then
        Api<AuthResponse> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo(mockAccessToken);
        verify(cookieService).setRefreshTokenCookie(any(), eq(mockRefreshToken));
    }

    private void setLoginConfigByProvider(final OAuthRequest request, final Long mockUserId) {
        switch (request.provider()) {
            case GITHUB -> when(githubService.signupWithLogin(request.oauthCode())).thenReturn(mockUserId);
            case GOOGLE -> when(googleService.signupWithLogin(request.oauthCode())).thenReturn(mockUserId);
            case KAKAO -> when(kakaoService.signupWithLogin(request.oauthCode())).thenReturn(mockUserId);
        }
    }

    @Test
    void OAuth_로그인시_잘못된_OAuth_Provider로_요청하면_MethodArgumentTypeMismatchException이_발생한다() {
        // given
        String path = PATH_PREFIX + "/oauth-login";
        String json = "{\"provider\": \"invalid\", \"oauthCode\": \"code\"}";

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, json);

        // then
        response.status(HttpStatus.BAD_REQUEST);
        response.body("code", equalTo(9004));
    }

    @Test
    void OAuth_로그인시_OAuth_Provider가_없으면_MethodArgumentNotValidException이_발생한다() {
        // given
        String path = PATH_PREFIX + "/oauth-login";
        OAuthRequest request = new OAuthRequest(null, "mock-code");

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, request);

        // then
        response.status(HttpStatus.BAD_REQUEST);
        response.body("code", equalTo(9001));
    }

    @Test
    void OAuth_로그인_시_인증_코드가_없으면_MethodArgumentNotValidException가_발생한다() {
        // given
        String path = PATH_PREFIX + "/oauth-login";
        OAuthRequest request = new OAuthRequest(OAuth2Provider.GITHUB, null);

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, request);

        // then
        response.status(HttpStatus.BAD_REQUEST);
        response.body("code", equalTo(9001));
    }

    @Test
    void 개발환경_구글_로그인이_성공한다() {
        // given
        OAuthRequest request = new OAuthRequest(OAuth2Provider.GOOGLE, "code");
        when(googleService.signupWithLoginDev("code")).thenReturn(1L);
        when(jwtProvider.issueAccessToken(1L)).thenReturn("mock-access-token");
        when(jwtProvider.issueRefreshToken(1L)).thenReturn("mock-refresh-token");
        doNothing().when(cookieService).setRefreshTokenCookie(any(), eq("mock-refresh-token"));

        // when
        String path = PATH_PREFIX + "/oauth-login-dev";
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, request);

        // then
        Api<AuthResponse> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo("mock-access-token");
        verify(cookieService).setRefreshTokenCookie(any(), eq("mock-refresh-token"));
    }

    @Test
    void 개발환경_깃허브_로그인이_성공한다() {
        // given
        OAuthRequest request = new OAuthRequest(OAuth2Provider.GITHUB, "code");
        when(githubService.signupWithLoginDev("code")).thenReturn(1L);
        when(jwtProvider.issueAccessToken(1L)).thenReturn("mock-access-token");
        when(jwtProvider.issueRefreshToken(1L)).thenReturn("mock-refresh-token");
        doNothing().when(cookieService).setRefreshTokenCookie(any(), eq("mock-refresh-token"));

        // when
        String path = PATH_PREFIX + "/oauth-login-dev";
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, request);

        // then
        Api<AuthResponse> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthApiCode.LOGIN_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo("mock-access-token");
        verify(cookieService).setRefreshTokenCookie(any(), eq("mock-refresh-token"));
    }

    @Test
    void 토큰_재발급_요청이_성공한다() {
        when(jwtProvider.getUserId("test-refresh-token")).thenReturn(1L);
        when(jwtProvider.issueAccessToken(1L)).thenReturn("mock-access-token");
        when(jwtProvider.issueRefreshToken(1L)).thenReturn("mock-refresh-token");

        // when
        String path = PATH_PREFIX + "/re-issue";
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        Api<AuthResponse> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthApiCode.REISSUE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthApiCode.REISSUE_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo("mock-access-token");
    }

}