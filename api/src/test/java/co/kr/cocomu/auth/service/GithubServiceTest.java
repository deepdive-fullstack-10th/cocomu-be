package co.kr.cocomu.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.auth.client.github.GithubApiClient;
import co.kr.cocomu.auth.client.github.GithubClient;
import co.kr.cocomu.auth.client.github.GithubUserResponse;
import co.kr.cocomu.auth.domain.OAuth2Provider;
import co.kr.cocomu.auth.domain.UserAuth;
import co.kr.cocomu.auth.repository.UserAuthJpaRepository;
import co.kr.cocomu.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock private GithubClient githubClient;
    @Mock private GithubApiClient githubApiClient;
    @Mock private UserAuthJpaRepository userAuthJpaRepository;

    @InjectMocks private GithubService githubService;

    private UserAuth mockAuth;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.createUser("test-user");
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        mockAuth = UserAuth.signUp("GITHUB_12345", mockUser, OAuth2Provider.GITHUB);
    }

    @Test
    void 신규_사용자는_회원가입_후_로그인한다() {
        // given
        String oauthCode = "mock-code";
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        GithubUserResponse githubUser = new GithubUserResponse("12345", "test-user");

        when(githubClient.getAccessToken(oauthCode)).thenReturn(tokenResponse);
        when(githubApiClient.getUser(tokenResponse)).thenReturn(githubUser);
        when(userAuthJpaRepository.findById("GITHUB_12345")).thenReturn(Optional.empty());
        when(userAuthJpaRepository.save(any(UserAuth.class))).thenReturn(mockAuth);

        // when
        Long userId = githubService.signupWithLogin(oauthCode);

        // then
        assertThat(userId).isEqualTo(mockUser.getId());
        verify(userAuthJpaRepository).save(any(UserAuth.class));
    }

    @Test
    void 개발환경으로_로그인이_된다() {
        // given
        String oauthCode = "mock-code";
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        GithubUserResponse githubUser = new GithubUserResponse("12345", "test-user");

        when(githubClient.getAccessTokenDev(oauthCode)).thenReturn(tokenResponse);
        when(githubApiClient.getUser(tokenResponse)).thenReturn(githubUser);
        when(userAuthJpaRepository.findById("GITHUB_12345")).thenReturn(Optional.empty());
        when(userAuthJpaRepository.save(any(UserAuth.class))).thenReturn(mockAuth);

        // when
        Long userId = githubService.signupWithLoginDev(oauthCode);

        // then
        assertThat(userId).isEqualTo(mockUser.getId());
        verify(userAuthJpaRepository).save(any(UserAuth.class));
    }

    @Test
    void 기존_사용자는_바로_로그인한다() {
        // given
        String oauthCode = "mock-code";
        TokenResponse tokenResponse = new TokenResponse("mock-token", "Bearer");
        GithubUserResponse githubUser = new GithubUserResponse("12345", "test-user");

        when(githubClient.getAccessToken(oauthCode)).thenReturn(tokenResponse);
        when(githubApiClient.getUser(tokenResponse)).thenReturn(githubUser);
        when(userAuthJpaRepository.findById("GITHUB_12345")).thenReturn(Optional.of(mockAuth));

        // when
        Long userId = githubService.signupWithLogin(oauthCode);

        // then
        assertThat(userId).isEqualTo(mockUser.getId());
        verify(userAuthJpaRepository, never()).save(any(UserAuth.class));
    }

}