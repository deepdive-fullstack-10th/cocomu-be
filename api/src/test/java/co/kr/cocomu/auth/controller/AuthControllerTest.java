package co.kr.cocomu.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.auth.dto.code.AuthCode;
import co.kr.cocomu.auth.dto.response.AuthResponse;
import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.auth.service.CookieService;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.common.jwt.JwtProvider;
import co.kr.cocomu.common.security.SecurityConfig;
import co.kr.cocomu.template.GetRequestTemplate;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(AuthController.class)
@WithMockUser
@Import(value = {SecurityConfig.class})
class AuthControllerTest {

    private static final String PATH_PREFIX = "/api/v1/auth";

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private CookieService cookieService;

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
        assertThat(result.code()).isEqualTo(AuthCode.LOGIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthCode.LOGIN_SUCCESS.getMessage());
        assertThat(result.result().accessToken()).isEqualTo(backdoorAccessToken);
        verify(cookieService).setRefreshTokenCookie(any(), eq(backdoorRefreshToken));
    }

    @Test
    @DisplayName("백도어 로그인이 성공하면 액세스 토큰을 반환한다")
    void 백도어_로그인이_실패한다() {
        // given
        String path = PATH_PREFIX + "/backdoor";
        String backdoorAccessToken = "backdoor-access-token";
        String backdoorRefreshToken = "backdoor-refresh-token";

        when(jwtProvider.issueAccessToken(1L)).thenReturn(backdoorAccessToken);
        when(jwtProvider.issueRefreshToken(1L)).thenReturn(backdoorRefreshToken);
        when(cookieService.isProdEnvironment()).thenReturn(true);
        doNothing().when(cookieService).setRefreshTokenCookie(any(), eq(backdoorRefreshToken));

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        ExceptionResponse result = response.status(HttpStatus.BAD_REQUEST).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(AuthExceptionCode.INVALID_ACCESS.getCode());
        assertThat(result.message()).isEqualTo(AuthExceptionCode.INVALID_ACCESS.getMessage());
    }

}