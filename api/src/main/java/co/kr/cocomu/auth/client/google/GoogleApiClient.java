package co.kr.cocomu.auth.client.google;

import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleApiClient {

    private final RestClient apiClient;

    public GoogleApiClient(final OAuthClientGenerator generator) {
        this.apiClient = generator.generateGoogleApiClient();
    }

    public GoogleUserResponse getUser(final TokenResponse tokenResponse) {
        return RestClientExecutor.execute(() -> apiClient.get()
                .uri("/oauth2/v3/userinfo")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(GoogleUserResponse.class));
    }

}