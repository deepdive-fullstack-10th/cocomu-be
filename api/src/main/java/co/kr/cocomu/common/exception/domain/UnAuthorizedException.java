package co.kr.cocomu.common.exception.domain;

import co.kr.cocomu.common.exception.dto.ExceptionCode;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException(ExceptionCode exceptionType) {
        super(exceptionType);
    }

}
