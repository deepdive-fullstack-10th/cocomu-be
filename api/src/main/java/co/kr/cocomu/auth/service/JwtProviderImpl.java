package co.kr.cocomu.auth.service;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.jwt.JwtProvider;
import co.kr.cocomu.auth.utils.DateUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey secretKey;
    private final long accessTokenExp;
    private final long refreshTokenExp;

    public JwtProviderImpl(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-exp}") long accessTokenPlusHour,
        @Value("${jwt.refresh-exp}") long refreshTokenPlusHour
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExp = accessTokenPlusHour;
        this.refreshTokenExp = refreshTokenPlusHour;
    }

    public String issueAccessToken(final Long userid) {
        final Date now = DateUtils.getDate();
        final Date expiryDate = DateUtils.getDate(now.getTime() + accessTokenExp);

        return Jwts.builder()
            .signWith(secretKey)
            .subject(String.valueOf(userid))
            .expiration(expiryDate)
            .issuedAt(now)
            .compact();
    }

    public String issueRefreshToken(final Long userid) {
        final Date now = DateUtils.getDate();
        final Date expiryDate = DateUtils.getDate(now.getTime() + refreshTokenExp);

        return Jwts.builder()
            .signWith(secretKey)
            .subject(String.valueOf(userid))
            .expiration(expiryDate)
            .issuedAt(now)
            .compact();
    }

    public void validationTokenWithThrow(final String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new UnAuthorizedException(AuthExceptionCode.EXPIRED_TOKEN);
        } catch (final SignatureException e) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_SIGNATURE);
        }  catch (final UnsupportedJwtException e) {
            throw new UnAuthorizedException(AuthExceptionCode.UNSUPPORTED_TOKEN);
        } catch (final MalformedJwtException e) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_TOKEN);
        } catch (final Exception e) {
            throw new UnAuthorizedException(AuthExceptionCode.TOKEN_ERROR);
        }
    }

    public Long getUserId(final String token) {
        try {
            final String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

            return Long.valueOf(subject);
        } catch (final NumberFormatException e) {
            throw new UnAuthorizedException(AuthExceptionCode.EXTRACTING_ERROR);
        }
    }

}
