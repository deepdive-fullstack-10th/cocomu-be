package co.kr.cocomu.auth.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

class OAuthClientGeneratorTest {

    private OAuthClientGenerator OAuthClientGenerator;
    private SimpleClientHttpRequestFactory requestFactory;

    @BeforeEach
    void setUp() {
        requestFactory = mock(SimpleClientHttpRequestFactory.class);
        OAuthClientGenerator = new OAuthClientGenerator(requestFactory);
    }

    @Test
    void 깃허브_API_Client가_생성된다() {
        // when
        RestClient gitHubApiClient = OAuthClientGenerator.generateGitHubApiClient();

        // then
        assertThat(gitHubApiClient).isNotNull();
    }

    @Test
    void 깃허브_Client가_생성된다() {
        // when
        RestClient githubClient = OAuthClientGenerator.generateGithubClient();

        // then
        assertThat(githubClient).isNotNull();
    }

    @Test
    void 카카오_Auth_Client가_생성된다() {
        // when
        RestClient kakaoAuthClient = OAuthClientGenerator.generateKakaoAuthClient();

        // then
        assertThat(kakaoAuthClient).isNotNull();
    }

    @Test
    void 카카오_API_Client가_생성된다() {
        // when
        RestClient kakaoApiClient = OAuthClientGenerator.generateKakaoApiClient();

        // then
        assertThat(kakaoApiClient).isNotNull();
    }

    @Test
    void 구글_OAuth_Client가_생성된다() {
        // when
        RestClient googleAuthClient = OAuthClientGenerator.generateGoogleOAuthClient();

        // then
        assertThat(googleAuthClient).isNotNull();
    }

    @Test
    void 구글_API_Client가_생성된다() {
        // when
        RestClient googleApiClient = OAuthClientGenerator.generateGoogleApiClient();

        // then
        assertThat(googleApiClient).isNotNull();
    }

}