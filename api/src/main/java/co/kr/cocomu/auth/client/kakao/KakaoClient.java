package co.kr.cocomu.auth.client.kakao;

import co.kr.cocomu.auth.client.FormDataGenerator;
import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final RestClient authClient;
    private final String clientId;
    private final String clientSecret;

    public KakaoClient(
            OAuthClientGenerator generator,
            @Value("${oauth.kakao.client-id}") String clientId,
            @Value("${oauth.kakao.client-secret}") String clientSecret
    ) {
        this.authClient = generator.generateKakaoAuthClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public TokenResponse getAccessToken(final String code) {
        return RestClientExecutor.execute(() -> authClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(FormDataGenerator.generateKakaoFormData(clientId, clientSecret, code))
                .retrieve()
                .body(TokenResponse.class));
    }

}