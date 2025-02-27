package co.kr.cocomu.common.exception.domain;

import co.kr.cocomu.common.exception.dto.ExceptionCode;

public class BadGatewayException extends BusinessException {

    public BadGatewayException(ExceptionCode exceptionType) {
        super(exceptionType);
    }

}
