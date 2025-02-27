package co.kr.cocomu.auth.dto.request;

import co.kr.cocomu.auth.domain.OAuth2Provider;
import jakarta.validation.constraints.NotNull;

public record OAuthRequest(
    @NotNull(message = "OAuthProvider는 필수입니다.") OAuth2Provider provider,
    @NotNull(message = "인증 코드는 필수입니다.") String oauthCode
) {
}
