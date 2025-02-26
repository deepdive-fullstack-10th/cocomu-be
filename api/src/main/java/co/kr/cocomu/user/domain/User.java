package co.kr.cocomu.user.domain;

import co.kr.cocomu.common.entity.TimeBaseEntity;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.user.dto.response.UserDto;
import co.kr.cocomu.user.exception.UserExceptionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "cocomu_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends TimeBaseEntity {

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn.cocomu.co.kr/images/profile.png";
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
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            return new User(nickname.substring(0, MAX_NICKNAME_LENGTH));
        }
        return new User(nickname);
    }

    private static void validateUserNickname(final String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new BadRequestException(UserExceptionCode.INVALID_NICKNAME);
        }
    }

    public UserDto toDto() {
        return new UserDto(id, nickname, profileImageUrl);
    }

}
