package co.kr.cocomu.codingspace.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodingSpaceExceptionCode implements ExceptionCode {

    MAX_USER_COUNT_IS_FOUR(4001, "코딩 스페이스의 최대 인원은 4명입니다."),
    MIN_USER_COUNT_IS_TWO(4002, "코딩 스페이스의 최소 인원은 2명입니다."),
    ;

    private final int code;
    private final String message;

    }
