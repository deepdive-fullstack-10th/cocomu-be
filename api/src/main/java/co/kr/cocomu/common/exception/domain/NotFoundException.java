package co.kr.cocomu.common.exception.domain;

import co.kr.cocomu.common.exception.dto.ExceptionCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(ExceptionCode exceptionType) {
        super(exceptionType);
    }

}
