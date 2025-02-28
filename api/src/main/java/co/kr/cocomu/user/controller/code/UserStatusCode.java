package co.kr.cocomu.user.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusCode implements ApiCode {

    USER_JOIN_SUCCESS(1000, "사용자 정보가 추가되었습니다."),
    USER_FOUND_SUCCESS(1000, "사용자 정보를 조회했습니다."),
    ALL_USER_FOUND_SUCCESS(1000, "모든 사용자 정보를 조회했습니다."),
    ;

    private int code;
    private String message;

}
