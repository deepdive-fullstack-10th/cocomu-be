package co.kr.cocomu.common.exception.domain;

import co.kr.cocomu.common.exception.dto.ExceptionCode;

public class BadRequestException extends BusinessException {

    public BadRequestException(ExceptionCode exceptionType) {
        super(exceptionType);
    }

}
