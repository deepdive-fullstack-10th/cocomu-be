package co.kr.cocomu.auth.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthApiCode implements ApiCode {

    LOGIN_SUCCESS(1100, "로그인에 성공했습니다."),
    REISSUE_SUCCESS(1100, "토큰 재발급에 성공했습니다.");

    private int code;
    private String message;

}
