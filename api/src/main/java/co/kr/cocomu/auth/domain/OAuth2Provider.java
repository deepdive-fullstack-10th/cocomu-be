package co.kr.cocomu.auth.domain;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;

public enum OAuth2Provider {

    GITHUB, KAKAO, GOOGLE;

    public static OAuth2Provider from(final String provider) {
        try {
            return valueOf(provider.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_PROVIDER);
        }
    }

}
