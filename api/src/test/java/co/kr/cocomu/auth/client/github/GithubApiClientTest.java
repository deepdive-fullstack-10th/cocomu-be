package co.kr.cocomu.auth.client.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GithubApiClientTest extends OAuthClientTemplate {

    private GithubApiClient githubApiClient;

    @BeforeEach
    void setUp() {
        githubApiClient = new GithubApiClient(fakeOAuthClientGenerator);
    }

    @Test
    void 깃허브_API를_통해_사용자_정보를_가져온다() {
        // given
        enqueue("{\"login\": \"test-user\", \"id\": 12345}");

        // when
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        GithubUserResponse response = githubApiClient.getUser(tokenResponse);

        // then
        assertThat(response.id()).isEqualTo("12345");
        assertThat(response.login()).isEqualTo("test-user");
    }


}