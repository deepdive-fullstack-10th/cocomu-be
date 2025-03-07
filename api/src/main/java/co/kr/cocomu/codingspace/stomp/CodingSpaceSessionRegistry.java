package co.kr.cocomu.codingspace.stomp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CodingSpaceSessionRegistry {

    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    public void registerSession(final String sessionId, final Long userId, final Long codingSpaceId) {
        sessions.put(sessionId, new SessionInfo(userId, codingSpaceId));
        log.info("세션 등록: sessionId={}, userId={}, tabId={}", sessionId, userId, codingSpaceId);
    }

    public SessionInfo getSessionInfo(final String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(final String sessionId) {
        sessions.remove(sessionId);
    }

}
