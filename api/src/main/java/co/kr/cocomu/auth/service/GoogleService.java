package co.kr.cocomu.auth.service;

import co.kr.cocomu.auth.client.google.GoogleApiClient;
import co.kr.cocomu.auth.client.google.GoogleClient;
import co.kr.cocomu.auth.client.google.GoogleUserResponse;
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
public class GoogleService {

    private final UserAuthJpaRepository userAuthJpaRepository;
    private final GoogleClient googleClient;
    private final GoogleApiClient googleApiClient;

    public Long signupWithLogin(final String code) {
        final TokenResponse tokenResponse = googleClient.getAccessToken(code);
        final GoogleUserResponse googleUser = googleApiClient.getUser(tokenResponse);

        final UserAuth userAuth = findOrSignup(googleUser);

        return userAuth.getUser().getId();
    }

    public Long signupWithLoginDev(final String code) {
        final TokenResponse tokenResponse = googleClient.getDevAccessToken(code);
        final GoogleUserResponse googleUser = googleApiClient.getUser(tokenResponse);

        final UserAuth userAuth = findOrSignup(googleUser);

        return userAuth.getUser().getId();
    }

    private UserAuth findOrSignup(final GoogleUserResponse googleUser) {
        final String providerId = "GOOGLE_" + googleUser.sub();
        return userAuthJpaRepository.findById(providerId)
                .orElseGet(() -> {
                    final User user = User.createUser(googleUser.name());
                    final UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GOOGLE);
                    return userAuthJpaRepository.save(userAuth);
                });
    }
}
