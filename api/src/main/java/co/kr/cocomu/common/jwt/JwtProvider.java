package co.kr.cocomu.common.jwt;

public interface JwtProvider {

    String issueAccessToken(Long userid);
    String issueRefreshToken(Long userid);
    void validationTokenWithThrow(String token);
    Long getUserId(String token);

}
