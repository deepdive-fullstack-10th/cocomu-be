package co.kr.cocomu.auth.client.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.BadGatewayException;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class GithubClientTest extends OAuthClientTemplate {

    private GithubClient githubClient;

    @BeforeEach
    void setUp() {
        githubClient = new GithubClient("github", "secret", "devid", "devSecret", fakeOAuthClientGenerator);
    }

    @Test
    void 깃허브_액세스토큰을_가져온다() {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");

        // when
        TokenResponse response = githubClient.getAccessToken("mock-code");

        // then
        assertThat(response.getToken()).isEqualTo("Bearer mock-token");
    }

    @Test
    void 깃허브_개발환경_액세스토큰을_가져온다() {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");

        // when
        TokenResponse response = githubClient.getAccessTokenDev("mock-code");

        // then
        assertThat(response.getToken()).isEqualTo("Bearer mock-token");
    }

    @Test
    void 설정한_정보로_깃허브_클라이언트에_요청한다() throws InterruptedException {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");
        githubClient.getAccessToken("mock-code");

        // when
        RecordedRequest request = takeRequest();

        // then
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/login/oauth/access_token");
        assertThat(request.getHeader("Content-Type")).contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        assertThat(request.getBody().readUtf8())
            .contains("client_id=github")
            .contains("client_secret=secret")
            .contains("code=mock-code");
    }

    @Test
    void OAuth에서_400번대_예러가_발생하면_BadGateway로_처리한다() {
        // given
        enqueue4XXError();

        // when & then: 예외 발생 검증
        assertThatThrownBy(() -> githubClient.getAccessToken("invalid-token"))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.OAUTH_EXCEPTION);
    }

    @Test
    void OAuth에서_500번대_에러가_발생하면_BadGateway로_처리한다() {
        // given
        enqueue5XXError();

        // when & then: 예외 발생 검증
        assertThatThrownBy(() -> githubClient.getAccessToken("invalid-token"))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.OAUTH_ERROR);
    }

}