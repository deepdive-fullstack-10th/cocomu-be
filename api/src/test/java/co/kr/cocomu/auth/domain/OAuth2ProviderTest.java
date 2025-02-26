package co.kr.cocomu.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OAuth2ProviderTest {

    @ParameterizedTest
    @ValueSource(strings = {"github", "Github", "GITHUB"})
    void 깃허브_Provider_생성을_한다(String providerName) {
        // given
        // when
        OAuth2Provider provider = OAuth2Provider.from(providerName);

        // then
        assertThat(provider).isEqualTo(OAuth2Provider.GITHUB);
    }

    @ParameterizedTest
    @ValueSource(strings = {"kakao", "Kakao", "KAKAO"})
    void 카카오_Provider_생성을_한다(String providerName) {
        // given
        // when
        OAuth2Provider provider = OAuth2Provider.from(providerName);

        // then
        assertThat(provider).isEqualTo(OAuth2Provider.KAKAO);
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "Google", "GOOGLE"})
    void 구글_Provider_생성을_한다(String providerName) {
        // given
        // when
        OAuth2Provider provider = OAuth2Provider.from(providerName);

        // then
        assertThat(provider).isEqualTo(OAuth2Provider.GOOGLE);
    }

    @Test
    void 잘못된_PROVIDER_인_경우_예외가_발생한다() {
        // given
        // when & then
        assertThatThrownBy(() -> OAuth2Provider.from("any"))
            .isInstanceOf(UnAuthorizedException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AuthExceptionCode.INVALID_PROVIDER);
    }

}