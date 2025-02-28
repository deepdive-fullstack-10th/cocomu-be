package co.kr.cocomu.auth.client.github;

import co.kr.cocomu.auth.client.FormDataGenerator;
import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {

    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;

    public GithubClient(
        @Value("${oauth.github.client-id}") final String clientId,
        @Value("${oauth.github.client-secret}") final String clientSecret,
        final OAuthClientGenerator generator
    ) {
        this.restClient = generator.generateGithubClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public TokenResponse getAccessToken(final String code) {
        return RestClientExecutor.execute(() -> restClient.post()
                .uri("/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(FormDataGenerator.generateGithubFormData(clientId, clientSecret, code))
                .retrieve()
                .body(TokenResponse.class));
    }

}
