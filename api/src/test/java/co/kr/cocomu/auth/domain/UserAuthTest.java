package co.kr.cocomu.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.user.domain.User;
import org.junit.jupiter.api.Test;

class UserAuthTest {

    User user = User.createUser("cocomu");

    @Test
    void 사용자_가입을_한다() {
        UserAuth userAuth = UserAuth.signUp("id", user, OAuth2Provider.GITHUB);

        assertThat(userAuth.getProviderId()).isEqualTo("id");
        assertThat(userAuth.getUser()).isEqualTo(user);
        assertThat(userAuth.getProvider()).isEqualTo(OAuth2Provider.GITHUB);
    }

}