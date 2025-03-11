package co.kr.cocomu.user.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExceptionCode implements ExceptionCode {

    USER_NOT_FOUND(1001, "사용자를 찾을 수 없습니다."),
    INVALID_NICKNAME(1002, "사용자 닉네임 정보가 잘못되었습니다."),
    ;

    private final int code;
    private final String message;

    }