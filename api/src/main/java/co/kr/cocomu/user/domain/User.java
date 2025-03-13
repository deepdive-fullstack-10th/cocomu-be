package co.kr.cocomu.user.domain;

import co.kr.cocomu.common.repository.TimeBaseEntity;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.user.dto.response.UserResponse;
import co.kr.cocomu.user.exception.UserExceptionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cocomu_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public class User extends TimeBaseEntity {

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn.cocomu.co.kr/images/default/profile.png";
    private static final int MAX_NICKNAME_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(nullable = false)
    private String profileImageUrl;

    protected User(final String nickname) {
        this.nickname = nickname;
        this.profileImageUrl = DEFAULT_PROFILE_IMAGE;
    }

    public static User createUser(final String nickname) {
        validateUserNickname(nickname);
        return new User(parseNickname(nickname));
    }

    private static String parseNickname(final String nickname) {
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            return nickname.substring(0, MAX_NICKNAME_LENGTH);
        }
        return nickname;
    }

    private static void validateUserNickname(final String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new BadRequestException(UserExceptionCode.INVALID_NICKNAME);
        }
    }

    public UserResponse toDto() {
        return new UserResponse(id, nickname, profileImageUrl);
    }

    public void updateProfile(final String nickname, final String profileImageUrl) {
        validateUserNickname(nickname);
        this.nickname = parseNickname(nickname);
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isNotDefaultImageUrl(final String nickname) {
        return !nickname.equals(DEFAULT_PROFILE_IMAGE);
    }

}
