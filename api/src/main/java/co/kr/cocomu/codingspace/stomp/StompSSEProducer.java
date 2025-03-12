package co.kr.cocomu.codingspace.stomp;

import co.kr.cocomu.codingspace.dto.message.EventMessage;
import co.kr.cocomu.codingspace.dto.message.EventType;
import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
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

    private static final String CODING_SPACE_PATH_FORMAT = "/sub/v1/coding-spaces/%s";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishUserEnter(final User user, final Long codingSpaceId) {
        final UserResponse data = user.toDto();
        final EventMessage<UserResponse> message = new EventMessage<>(EventType.USER_ENTER, data);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===사용자 입장 알림 발행===> {}", message);
    }

    public void publishUserLeave(final User user, final Long codingSpaceId) {
        final UserResponse data = user.toDto();
        final EventMessage<UserResponse> message = new EventMessage<>(EventType.USER_LEAVE, data);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===사용자 퇴장 알림 발행===> {}", message);
    }

    public void publishStartSpace(final Long codingSpaceId) {
        final EventMessage<Void> message = new EventMessage<>(EventType.STUDY_START, null);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===스터디 시작 알림 발행===> {}", message);
    }

    public void publishFeedback(final Long codingSpaceId) {
        final EventMessage<Void> message = new EventMessage<>(EventType.STUDY_FEEDBACK, null);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===스터디 피드백 알림 발행===> {}", message);
    }

    public void publishFinish(final Long codingSpaceId) {
        final EventMessage<Void> message = new EventMessage<>(EventType.STUDY_FINISH, null);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===스터디 종료 알림 발행===> {}", message);
    }

    public void publishAddTestCase(final TestCaseDto testCaseDto, final Long codingSpaceId) {
        final EventMessage<TestCaseDto> message = new EventMessage<>(EventType.ADD_TEST_CASE, testCaseDto);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===테스트 케이스 추가 알림 발행===> {}", message);
    }

    public void publishDeleteTestCase(final Long codingSpaceId, final Long testCaseId) {
        final EventMessage<Long> message = new EventMessage<>(EventType.DELETE_TEST_CASE, testCaseId);
        final String destination = String.format(CODING_SPACE_PATH_FORMAT, codingSpaceId);
        messagingTemplate.convertAndSend(destination, message);
        log.info("===테스트 케이스 삭제 알림 발행===> {}", message);
    }

}
