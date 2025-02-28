package co.kr.cocomu.auth.client.kakao;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KakaoApiClientTest extends OAuthClientTemplate {

    private KakaoApiClient kakaoApiClient;

    @BeforeEach
    void setUp() {
        kakaoApiClient = new KakaoApiClient(fakeOAuthClientGenerator);
    }

    @Test
    void 카카오_API를_통해_사용자_정보를_가져온다() {
        // given
        enqueue("{\"id\": \"12345\", \"properties\": { \"nickname\": \"test-user\" } }");

        // when
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        KakaoUserResponse response = kakaoApiClient.getUser(tokenResponse);

        // then
        assertThat(response.id()).isEqualTo("12345");
        assertThat(response.getNickname()).isEqualTo("test-user");
    }

}