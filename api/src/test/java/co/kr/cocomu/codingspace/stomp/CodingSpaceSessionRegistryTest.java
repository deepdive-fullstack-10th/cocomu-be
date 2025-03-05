package co.kr.cocomu.codingspace.stomp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

class CodingSpaceSessionRegistryTest {

    @Test
    void 세션_등록_조회() {
        // given
        CodingSpaceSessionRegistry registry = new CodingSpaceSessionRegistry();
        String sessionId = "test-session-id";
        Long userId = 123L;
        Long codingSpaceId = 456L;

        // when
        registry.registerSession(sessionId, userId, codingSpaceId);

        // then
        SessionInfo info = registry.getSessionInfo(sessionId);
        assertThat(info).isNotNull();
        assertThat(info.userId()).isEqualTo(userId);
        assertThat(info.codingSpaceId()).isEqualTo(codingSpaceId);
    }

    @Test
    void 존재하지_않는_세션_조회시_null_반환() {
        // given
        CodingSpaceSessionRegistry registry = new CodingSpaceSessionRegistry();
        String nonExistingSessionId = "non-existing-id";

        // when
        SessionInfo info = registry.getSessionInfo(nonExistingSessionId);

        // then
        assertThat(info).isNull();
    }

    @Test
    void 세션_정보_덮어쓰기() {
        // given
        CodingSpaceSessionRegistry registry = new CodingSpaceSessionRegistry();
        String sessionId = "test-session-id";

        registry.registerSession(sessionId, 123L, 456L);

        Long newUserId = 789L;
        Long newCodingSpaceId = 101112L;

        // when
        registry.registerSession(sessionId, newUserId, newCodingSpaceId);

        // then
        SessionInfo info = registry.getSessionInfo(sessionId);
        assertThat(info.userId()).isEqualTo(newUserId);
        assertThat(info.codingSpaceId()).isEqualTo(newCodingSpaceId);
    }

    @Test
    void 세션_삭제() {
        // given
        CodingSpaceSessionRegistry registry = new CodingSpaceSessionRegistry();
        registry.registerSession("id", 123L, 456L);

        // when
        registry.removeSession("id");

        // then
        assertThat(registry.getSessionInfo("id")).isNull();
    }

    @Test
    void 존재하지_않는_세션_삭제_오류없음() {
        // given
        CodingSpaceSessionRegistry registry = new CodingSpaceSessionRegistry();
        String nonExistingSessionId = "non-existing-id";

        // when & then
        assertThatCode(() -> registry.removeSession(nonExistingSessionId))
            .doesNotThrowAnyException();
    }

}