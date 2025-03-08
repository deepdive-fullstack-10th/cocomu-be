package co.kr.cocomu.codingspace.stomp;

import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompEventListener {

    private static final String CODING_SPACE_DEST_PREFIX = "/sub/v1/coding-spaces/";
    private final CodingSpaceSessionRegistry codingSpaceSessionRegistry;
    private final CodingSpaceCommandService codingSpaceCommandService;

    @EventListener
    public void handleSubscription(final SessionSubscribeEvent event) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        final String sessionId = accessor.getSessionId();
        final String destination = accessor.getDestination();
        if (sessionId != null && destination != null && destination.startsWith(CODING_SPACE_DEST_PREFIX)) {
            final Long userId = extractUserId(accessor);
            final Long codingSpaceId = extractCodingSpaceId(destination);
            codingSpaceSessionRegistry.registerSession(sessionId, userId, codingSpaceId);
            log.info("STOMP 구독 처리: sessionId={}, userId={}, tabId={}", sessionId, userId, codingSpaceId);
        }
    }

    @EventListener
    public void handleDisconnect(final SessionDisconnectEvent event) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        final String sessionId = accessor.getSessionId();

        if (sessionId != null) {
            final SessionInfo sessionInfo = codingSpaceSessionRegistry.getSessionInfo(sessionId);
            codingSpaceCommandService.leaveSpace(sessionInfo.userId(), sessionInfo.codingSpaceId());
            codingSpaceSessionRegistry.removeSession(sessionId);
        }
    }

    private static @NotNull Long extractCodingSpaceId(final String destination) {
        final String codingSpaceIdStr = destination.substring(CODING_SPACE_DEST_PREFIX.length());
        return Long.parseLong(codingSpaceIdStr.split("/")[0]);
    }

    private static @NotNull Long extractUserId(final StompHeaderAccessor accessor) {
        final Principal principal = accessor.getUser();

        if (principal instanceof final Authentication authentication) {
            return (Long) authentication.getPrincipal();
        }
        throw new IllegalStateException();
    }

}
