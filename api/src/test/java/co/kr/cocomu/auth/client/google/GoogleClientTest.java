package co.kr.cocomu.auth.client.google;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class GoogleClientTest extends OAuthClientTemplate {

    private GoogleClient googleClient;

    @BeforeEach
    void setUp() {
        googleClient = new GoogleClient("google", "secret", "uri", "devuri", fakeOAuthClientGenerator);
    }

    @Test
    void 구글_액세스토큰을_가져온다() {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");

        // when
        TokenResponse response = googleClient.getAccessToken("mock-code");

        // then
        assertThat(response.getToken()).isEqualTo("Bearer mock-token");
    }

    @Test
    void 구글_개발환경_액세스토큰을_가져온다() {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");

        // when
        TokenResponse response = googleClient.getDevAccessToken("mock-code");

        // then
        assertThat(response.getToken()).isEqualTo("Bearer mock-token");
    }

    @Test
    void 설정한_정보로_구글_클라이언트에_요청한다() throws InterruptedException {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");
        googleClient.getAccessToken("mock-code");

        // when
        RecordedRequest request = takeRequest();

        // then
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/token");
        assertThat(request.getHeader("Content-Type")).contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        assertThat(request.getBody().readUtf8())
            .contains("client_id=google")
            .contains("client_secret=secret")
            .contains("code=mock-code")
            .contains("grant_type=authorization_code")
            .contains("redirect_uri=uri");
    }

}