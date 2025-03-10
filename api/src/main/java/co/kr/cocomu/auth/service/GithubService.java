package co.kr.cocomu.auth.service;

import co.kr.cocomu.auth.client.github.GithubApiClient;
import co.kr.cocomu.auth.client.github.GithubClient;
import co.kr.cocomu.auth.client.github.GithubUserResponse;
import co.kr.cocomu.auth.client.TokenResponse;
import co.kr.cocomu.auth.domain.OAuth2Provider;
import co.kr.cocomu.auth.domain.UserAuth;
import co.kr.cocomu.auth.repository.UserAuthJpaRepository;
import co.kr.cocomu.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GithubService {

    private final UserAuthJpaRepository userAuthJpaRepository;
    private final GithubClient githubClient;
    private final GithubApiClient githubApiClient;

    public Long signupWithLogin(final String oauthCode) {
        final TokenResponse tokenResponse = githubClient.getAccessToken(oauthCode);
        final GithubUserResponse githubUser = githubApiClient.getUser(tokenResponse);

        final UserAuth userAuth = findOrSignup(githubUser);

        return userAuth.getUser().getId();
    }

    public Long signupWithLoginDev(final String oauthCode) {
        final TokenResponse tokenResponse = githubClient.getAccessTokenDev(oauthCode);
        final GithubUserResponse githubUser = githubApiClient.getUser(tokenResponse);

        final UserAuth userAuth = findOrSignup(githubUser);

        return userAuth.getUser().getId();
    }

    private UserAuth findOrSignup(final GithubUserResponse githubUser) {
        final String providerId = "GITHUB_" + githubUser.id();

        return userAuthJpaRepository.findById(providerId).orElseGet(() -> {
            final User user = User.createUser(githubUser.login());
            final UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GITHUB);
            return userAuthJpaRepository.save(userAuth);
        });
    }

}
