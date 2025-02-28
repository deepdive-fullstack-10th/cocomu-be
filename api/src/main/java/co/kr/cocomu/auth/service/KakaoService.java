package co.kr.cocomu.auth.service;

import co.kr.cocomu.auth.client.kakao.KakaoApiClient;
import co.kr.cocomu.auth.client.kakao.KakaoClient;
import co.kr.cocomu.auth.client.kakao.KakaoUserResponse;
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
public class KakaoService {

    private final UserAuthJpaRepository userAuthJpaRepository;
    private final KakaoClient kakaoClient;
    private final KakaoApiClient kakaoApiClient;

    public Long signupWithLogin(final String code) {
        final TokenResponse tokenResponse = kakaoClient.getAccessToken(code);
        final KakaoUserResponse kakaoUser = kakaoApiClient.getUser(tokenResponse);

        final UserAuth userAuth = findOrSignup(kakaoUser);

        return userAuth.getUser().getId();
    }

    private UserAuth findOrSignup(final KakaoUserResponse kakaoUser) {
        final String providerId = "KAKAO_" + kakaoUser.id();
        return userAuthJpaRepository.findById(providerId)
                .orElseGet(() -> {
                    final User user = User.createUser(kakaoUser.getNickname());
                    final UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.KAKAO);
                    return userAuthJpaRepository.save(userAuth);
                });
    }
}
