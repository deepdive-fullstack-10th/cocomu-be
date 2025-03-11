package co.kr.cocomu.common.security;

import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.exception.dto.ErrorResponse;
import co.kr.cocomu.common.exception.dto.ExceptionCode;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (final UnAuthorizedException e) {
            log.error("JWT 인증 예외 발생 - {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ExceptionCode exception = e.getExceptionType();
            final ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getCode(), e.getMessage());
            objectMapper.writeValue(response.getWriter(), exceptionResponse);
        }
    }

}
