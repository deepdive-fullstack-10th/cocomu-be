package co.kr.cocomu.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.auth.utils.DateUtils;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.jwt.JwtProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private final String key = "1".repeat(32);
    private final long accessTokenExpHours = 24 * 3600000;
    private final long refreshTokenExpHours = 24 * 7 * 3600000;
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProviderImpl(key, accessTokenExpHours, refreshTokenExpHours);
    }

    @Test
    void 사용자id로_액세스_토큰을_발급한다() {
        // given
        Long userId = 1L;

        // when
        String accessToken = jwtProvider.issueAccessToken(userId);

        // then
        assertThat(jwtProvider.getUserId(accessToken)).isEqualTo(userId);
        assertThatCode(() -> jwtProvider.validationTokenWithThrow(accessToken))
            .doesNotThrowAnyException();
    }

    @Test
    void 사용자id로_리프레쉬_토큰을_발급한다() {
        // given
        Long userId = 1L;

        // when
        String refreshToken = jwtProvider.issueRefreshToken(userId);

        // then
        assertThat(jwtProvider.getUserId(refreshToken)).isEqualTo(userId);
        assertThatCode(() -> jwtProvider.validationTokenWithThrow(refreshToken))
            .doesNotThrowAnyException();
    }

    @Test
    void 토큰시간이_만료되면_예외가_발생한다() {
        try (MockedStatic<DateUtils> mockedDateUtils = mockStatic(DateUtils.class)) {
            // given
            Date now = new Date();
            Date after = new Date(now.getTime() - 1);
            mockedDateUtils.when(DateUtils::getDate).thenReturn(now);
            mockedDateUtils.when(() -> DateUtils.getDate(anyLong())).thenReturn(after);

            String accessToken = jwtProvider.issueAccessToken(1L);

            // when & then
            assertThatThrownBy(() -> jwtProvider.validationTokenWithThrow(accessToken))
                .isInstanceOf(UnAuthorizedException.class)
                .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.EXPIRED_TOKEN);
        }
    }

    @Test
    void JWT_KEY가_다르다면_예외가_발생한다() {
        // given
        String otherKey = "2".repeat(32);
        JwtProvider otherJwtProvider = new JwtProviderImpl(otherKey, accessTokenExpHours, refreshTokenExpHours);
        String accessToken = otherJwtProvider.issueAccessToken(1L);

        // when & then
        assertThatThrownBy(() -> jwtProvider.validationTokenWithThrow(accessToken))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.INVALID_SIGNATURE);
    }

    @Test
    void 지원하지_않는_JWT_형식이면_예외가_발생한다() {
        // given
        // 의도적으로 지원하지 않는 알고리즘을 사용
        String unsupportedToken = "eyJhbGciOiJub25lIn0K.eyJzdWIiOiIxIn0.";

        // when & then
        assertThatThrownBy(() -> jwtProvider.validationTokenWithThrow(unsupportedToken))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.UNSUPPORTED_TOKEN);
    }

    @Test
    void 형식이_잘못된_토큰이면_예외가_발생한다() {
        // given
        String malformedToken = "invalid.jwt.token";

        // when & then
        assertThatThrownBy(() -> jwtProvider.validationTokenWithThrow(malformedToken))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.INVALID_TOKEN);
    }

    @Test
    void 기타_예외가_발생하면_TOKEN_ERROR_예외가_발생한다() {
        // given
        String nullToken = null;

        // when & then
        assertThatThrownBy(() -> jwtProvider.validationTokenWithThrow(nullToken))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.TOKEN_ERROR);
    }

    @Test
    void getUserId_숫자가_아닌_subject가_포함된_토큰이면_예외가_발생한다() {
        // given
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        String nonNumericSubjectToken = Jwts.builder()
            .subject("not-a-number")
            .signWith(secretKey)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtProvider.getUserId(nonNumericSubjectToken))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.EXTRACTING_ERROR);
    }

}