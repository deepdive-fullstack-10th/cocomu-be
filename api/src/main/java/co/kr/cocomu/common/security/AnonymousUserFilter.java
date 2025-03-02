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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AnonymousUserFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Anonymous UserFilter ==========> uri: {}, header: {}", request.getRequestURI(), request.getHeader(HttpHeaders.AUTHORIZATION));
        Long userId = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(this::extractToken)
            .map(this::getUserId)
            .orElseGet(() -> {
                log.info("Anonymous User 진입 ======>");
                return null;
            });

        setAuthentication(userId);
        filterChain.doFilter(request, response);
    }

    private Long getUserId(String token) {
        jwtProvider.validationTokenWithThrow(token);
        return jwtProvider.getUserId(token);
    }

    private static void setAuthentication(Long userId) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractToken(String authToken) {
        if (!authToken.startsWith("Bearer ")) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_TOKEN);
        }

        return authToken.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String path = request.getRequestURI();
        return !SecurityPathConfig.isAnonymousUri(path);
    }

}
