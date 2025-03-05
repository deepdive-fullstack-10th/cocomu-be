package co.kr.cocomu.codingspace.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodingSpaceExceptionCode implements ExceptionCode {

    MAX_USER_COUNT_IS_FOUR(4001, "코딩 스페이스의 최대 인원은 4명입니다."),
    MIN_USER_COUNT_IS_TWO(4002, "코딩 스페이스의 최소 인원은 2명입니다."),
    NOT_FOUND_SPACE(4003, "존재하지 않는 코딩 스페이스입니다."),
    ALREADY_PARTICIPATION_SPACE(4004, "이미 코딩 스페이스에 참여되었습니다."),
    NOT_WAITING_STUDY(4005, "대기중인 코딩 스페이스가 아닙니다."),
    OVER_USER_COUNT(4006, "코딩 스페이스 최대 인원을 초과헀습니다."),
    FINISHED_CODING_SPACE(4007, "종료된 코딩 스페이스입니다."),
    NO_PARTICIPATION_SPACE(4008, "코딩 스페이스에 참여중이지 않습니다."),
    ;

    private final int code;
    private final String message;

    }
