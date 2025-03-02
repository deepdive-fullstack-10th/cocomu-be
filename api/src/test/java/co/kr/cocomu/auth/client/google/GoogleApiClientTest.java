package co.kr.cocomu.auth.client.google;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.common.template.OAuthClientTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoogleApiClientTest extends OAuthClientTemplate {

    private GoogleApiClient googleApiClient;

    @BeforeEach
    void setUp() {
        googleApiClient = new GoogleApiClient(fakeOAuthClientGenerator);
    }

    @Test
    void 구글_API를_통해_사용자_정보를_가져온다() {
        // given
        enqueue("{\"sub\": \"test-user\", \"name\": 12345}");

        // when
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        GoogleUserResponse response = googleApiClient.getUser(tokenResponse);

        // then
        assertThat(response.name()).isEqualTo("12345");
        assertThat(response.sub()).isEqualTo("test-user");
    }

}