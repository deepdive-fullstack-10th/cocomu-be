package co.kr.cocomu.auth.client.kakao;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class KakaoClientTest extends OAuthClientTemplate {

    private KakaoClient kakaoClient;

    @BeforeEach
    void setUp() {
        kakaoClient = new KakaoClient(fakeOAuthClientGenerator, "kakao", "secret");
    }

    @Test
    void 카카오_액세스토큰을_가져온다() {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");

        // when
        TokenResponse response = kakaoClient.getAccessToken("mock-code");

        // then
        assertThat(response.getToken()).isEqualTo("Bearer mock-token");
    }

    @Test
    void 설정한_정보로_카카오_클라이언트에_요청한다() throws InterruptedException {
        // given
        enqueue("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\"}");
        kakaoClient.getAccessToken("mock-code");

        // when
        RecordedRequest request = takeRequest();

        // then
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/oauth/token");
        assertThat(request.getHeader("Content-Type")).contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        assertThat(request.getBody().readUtf8())
            .contains("client_id=kakao")
            .contains("client_secret=secret")
            .contains("code=mock-code")
            .contains("grant_type=authorization_code");
    }


}