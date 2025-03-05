package co.kr.cocomu.common.stomp;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.UnAuthorizedException;
import co.kr.cocomu.common.jwt.JwtProvider;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RequiredArgsConstructor
public class StompFilter implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            final String bearerToken = accessor.getFirstNativeHeader("Authorization");
            final String token = extractToken(bearerToken);

            jwtProvider.validationTokenWithThrow(token);
            final Long userId = jwtProvider.getUserId(token);
            accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()));
        }
        return message;
    }

    private String extractToken(final String authToken) {
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            throw new UnAuthorizedException(AuthExceptionCode.INVALID_TOKEN);
        }

        return authToken.substring(7);
    }

}
