package co.kr.cocomu.common.fake;

import co.kr.cocomu.auth.client.OAuthClientGenerator;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class FakeOAuthClientGenerator extends OAuthClientGenerator {

    private final RestClient restClient;

    public FakeOAuthClientGenerator(MockWebServer mockWebServer) {
        super(new SimpleClientHttpRequestFactory());
        this.restClient = RestClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build();
    }

    @Override
    public RestClient generateGitHubApiClient() {
        return restClient;
    }

    @Override
    public RestClient generateGithubClient() {
        return restClient;
    }

    @Override
    public RestClient generateKakaoAuthClient() {
        return restClient;
    }

    @Override
    public RestClient generateKakaoApiClient() {
        return restClient;
    }

    @Override
    public RestClient generateGoogleOAuthClient() {
        return restClient;
    }

    @Override
    public RestClient generateGoogleApiClient() {
        return restClient;
    }

}
