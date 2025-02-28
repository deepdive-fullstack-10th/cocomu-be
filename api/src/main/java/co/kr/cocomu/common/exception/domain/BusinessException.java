package co.kr.cocomu.common.exception.domain;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionCode exceptionType;

    public BusinessException(ExceptionCode exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

}
