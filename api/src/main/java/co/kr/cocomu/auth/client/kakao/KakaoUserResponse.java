package co.kr.cocomu.auth.client.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
    String id,
    Properties properties
) {

    public record Properties(String nickname) {}

    public String getNickname() {
        return properties.nickname();
    }

}