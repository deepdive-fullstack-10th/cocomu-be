package co.kr.cocomu.auth.client.github;

import co.kr.cocomu.auth.client.RestClientExecutor;
import co.kr.cocomu.auth.client.OAuthClientGenerator;
import co.kr.cocomu.auth.client.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubApiClient {

    private final RestClient apiClient;

    public GithubApiClient(final OAuthClientGenerator generator) {
        this.apiClient = generator.generateGitHubApiClient();
    }

    public GithubUserResponse getUser(final TokenResponse tokenResponse) {
        return RestClientExecutor.execute(() -> apiClient.get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(GithubUserResponse.class));
    }

}
