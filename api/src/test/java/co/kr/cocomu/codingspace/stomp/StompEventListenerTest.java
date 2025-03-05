package co.kr.cocomu.codingspace.stomp;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@ExtendWith(MockitoExtension.class)
class StompEventListenerTest {

    @Mock private CodingSpaceSessionRegistry codingSpaceSessionRegistry;
    @Mock private CodingSpaceCommandService codingSpaceCommandService;

    @InjectMocks private StompEventListener stompEventListener;

    @Test
    void 구독_이벤트_처리_성공() {
        // given
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId("test-session-id");
        accessor.setDestination("/sub/v1/coding-spaces/123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(456L);
        accessor.setUser(authentication);

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when
        stompEventListener.handleSubscription(event);

        // then
        verify(codingSpaceSessionRegistry).registerSession("test-session-id", 456L, 123L);
    }

    @Test
    void 세션_ID가_없으면_구독_처리_안함() {
        // given
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when
        stompEventListener.handleSubscription(event);

        // then
        verify(codingSpaceSessionRegistry, never()).registerSession(any(), any(), any());
    }

    @Test
    void dest_정보가_없으면_구독처리_안함() {
        // given
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId("test-session-id");

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when
        stompEventListener.handleSubscription(event);

        // then
        verify(codingSpaceSessionRegistry, never()).registerSession(any(), any(), any());
    }

    @Test
    void dest_목적지가_다르면_구독에_대한_세션_처리_안함() {
        // given
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId("test-session-id");
        accessor.setDestination("/sub/no");

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when
        stompEventListener.handleSubscription(event);

        // then
        verify(codingSpaceSessionRegistry, never()).registerSession(any(), any(), any());
    }

    @Test
    void 연결종료_이벤트_처리_성공() {
        // given
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        accessor.setSessionId("test-session-id");

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        SessionInfo sessionInfo = new SessionInfo(456L, 123L);
        when(codingSpaceSessionRegistry.getSessionInfo("test-session-id")).thenReturn(sessionInfo);

        doNothing().when(codingSpaceCommandService).leaveSpace(456L, 123L);

        // when
        stompEventListener.handleDisconnect(event);

        // then
        verify(codingSpaceCommandService).leaveSpace(456L, 123L);
        verify(codingSpaceSessionRegistry).removeSession("test-session-id");
    }

    @Test
    void 연결_종료_시_세션_정보_없으면_처리_안함() {
        // given
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when
        stompEventListener.handleDisconnect(event);

        // then
        verify(codingSpaceSessionRegistry, never()).registerSession(any(), any(), any());
    }

    @Test
    void 구독_이벤트_처리_사용자ID추출_실패() {
        // given
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId("test-session-id");
        accessor.setDestination("/sub/v1/coding-spaces/123");

        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        when(event.getMessage()).thenReturn(message);

        // when & then
        assertThatThrownBy(() -> stompEventListener.handleSubscription(event))
            .isInstanceOf(IllegalStateException.class);
        verify(codingSpaceSessionRegistry, never()).registerSession(anyString(), anyLong(), anyLong());
    }

}