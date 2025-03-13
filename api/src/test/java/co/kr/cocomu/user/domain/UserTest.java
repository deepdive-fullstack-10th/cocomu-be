package co.kr.cocomu.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.user.exception.UserExceptionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class UserTest {

    @Test
    void 사용자_생성시_닉네임_정보가_필요하다() {
        // given
        String nickname = "닉".repeat(10);
        // when
        User user = User.createUser(nickname);
        // then
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @Test
    void 사용자_생성시_최대_닉네임_길이만큼_제한된다() {
        // given
        String nickname = "닉".repeat(11);
        // when
        User user = User.createUser(nickname);
        // then
        nickname = "닉".repeat(10);
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 사용자_닉네임은_공백이거나_null_일_수_없다(String nickname) {
        // given
        // when & then
        assertThatThrownBy(() -> User.createUser(nickname))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", UserExceptionCode.INVALID_NICKNAME);
    }

    @Test
    void 사용자_프로필_정보가_수정된다() {
        // given
        String nickname = "닉".repeat(8);
        User user = User.createUser(nickname);

        // when
        user.updateProfile("닉네임", "profileImageUrl");

        // then
        assertThat(user.getNickname()).isEqualTo("닉네임");
        assertThat(user.getProfileImageUrl()).isEqualTo("profileImageUrl");
    }

    @Test
    void 기본_프로필이_아니라면_True() {
        // given
        User user = User.createUser("닉네임");

        // when
        boolean result = user.isNotDefaultImageUrl("string");

        // then
        assertThat(result).isTrue();
    }


    @Test
    void 기본_프로필이라면_False() {
        // given
        User user = User.createUser("닉네임");

        // when
        String defaultUrl = "https://cdn.cocomu.co.kr/images/default/profile.png";
        boolean result = user.isNotDefaultImageUrl(defaultUrl);

        // then
        assertThat(result).isFalse();
    }

}