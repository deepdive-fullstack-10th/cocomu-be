package co.kr.cocomu.admin.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AdminExceptionCode implements ExceptionCode {

    NOT_FOUND_WORKBOOK(10001, "스터디 문제집 정보에 포함되어 있지 않은 문제집입니다."),
    NOT_FOUND_LANGUAGE(10002, "스터디 언어 정보에 포함되어 있지 않은 언어입니다."),
    ;

    private final int code;
    private final String message;

}
