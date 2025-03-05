package co.kr.cocomu.codingspace.producer;

import co.kr.cocomu.codingspace.dto.message.EventMessage;
import co.kr.cocomu.codingspace.dto.message.EventType;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class StompSSEProducer {

    private static final String CODING_SPACE_PATH_FORMAT = "/sub/coding-spaces/%s";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishUserEnter(final User user, final Long codingSpaceId) {
        log.info("===사용자 입장 알림 발행===");
        final UserResponse data = user.toDto();
        EventMessage<UserResponse> message = new EventMessage<>(EventType.USER_ENTER, data);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
    }

}
