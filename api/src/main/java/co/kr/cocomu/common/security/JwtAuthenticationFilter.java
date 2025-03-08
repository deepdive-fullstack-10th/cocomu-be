package co.kr.cocomu.common.security;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("request ==============> uri: {}", request.getRequestURI());
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token = extractToken(bearerToken);
        setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        jwtProvider.validationTokenWithThrow(token);
        final Long userId = jwtProvider.getUserId(token);

        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractToken(final String authToken) {
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_TOKEN);
        }

        return authToken.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String path = request.getRequestURI();
        return SecurityPathConfig.isPublicUri(path)
            || SecurityPathConfig.isAnonymousUri(path);
    }

}
