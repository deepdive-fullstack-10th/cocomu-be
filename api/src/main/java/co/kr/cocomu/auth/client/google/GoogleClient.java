package co.kr.cocomu.auth.client.google;

import co.kr.cocomu.auth.client.FormDataGenerator;
import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class GoogleClient {

    private final RestClient authClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String devRedirectUri;

    public GoogleClient(
        @Value("${oauth.google.client-id}") final String clientId,
        @Value("${oauth.google.client-secret}") final String clientSecret,
        @Value("${oauth.google.redirect-uri}") final String redirectUri,
        @Value("${oauth.google.redirect-uri-dev}") final String devRedirectUri,
        final OAuthClientGenerator generator
    ) {
        this.authClient = generator.generateGoogleOAuthClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.devRedirectUri = devRedirectUri;
    }

    public TokenResponse getAccessToken(final String code) {
        return RestClientExecutor.execute(() -> authClient.post()
            .uri("/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(FormDataGenerator.generateGoogleFormData(clientId, clientSecret, code, redirectUri))
            .retrieve()
            .body(TokenResponse.class));
    }

    public TokenResponse getDevAccessToken(final String code) {
        return RestClientExecutor.execute(() -> authClient.post()
            .uri("/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(FormDataGenerator.generateGoogleFormData(clientId, clientSecret, code, devRedirectUri))
            .retrieve()
            .body(TokenResponse.class));
    }

}