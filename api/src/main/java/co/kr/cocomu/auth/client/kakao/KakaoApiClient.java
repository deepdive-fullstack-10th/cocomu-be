package co.kr.cocomu.auth.client.kakao;

import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient {

    private final RestClient apiClient;

    public KakaoApiClient(final OAuthClientGenerator generator) {
        this.apiClient = generator.generateKakaoApiClient();
    }

    public KakaoUserResponse getUser(final TokenResponse tokenResponse) {
        return RestClientExecutor.execute(() -> apiClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(KakaoUserResponse.class));
    }

}