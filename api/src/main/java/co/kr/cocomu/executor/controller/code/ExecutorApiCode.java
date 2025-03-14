package co.kr.cocomu.executor.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExecutorApiCode implements ApiCode {

    EXECUTE_CODE_SUCCESS(5000, "코드 실행이 성공했습니다."),
    SUBMIT_CODE_SUCCESS(5000, "코드 제출이 성공했습니다."),
    ;

    private final int code;
    private final String message;

}
