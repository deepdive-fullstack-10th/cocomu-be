package co.kr.cocomu.auth.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TokenResponse(String accessToken, String tokenType) {

    public String getToken() {
        return tokenType + " " + accessToken;
    }

}