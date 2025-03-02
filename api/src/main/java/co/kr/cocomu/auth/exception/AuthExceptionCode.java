package co.kr.cocomu.auth.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthExceptionCode implements ExceptionCode {

    INVALID_PROVIDER(1101, "제공되지 않는 OAuth Service 입니다"),
    EXPIRED_TOKEN(1102, "토큰의 유효 시간이 만료되었습니다."),
    INVALID_SIGNATURE(1103, "유효하지 않은 토큰 서명입니다."),
    UNSUPPORTED_TOKEN(1104, "지원되지 않는 토큰 유형입니다."),
    INVALID_TOKEN(1105, "잘못된 형식의 토큰입니다."),
    TOKEN_ERROR(1106, "알 수 없는 토큰 에러입니다."),
    EXTRACTING_ERROR(1107, "토큰 정보를 추출 할 수 없습니다."),
    OAUTH_EXCEPTION(1109, "OAuth 로그인에 실패했습니다."),
    OAUTH_ERROR(1110, "OAuth 로그인 중 알 수 없는 에러가 발생했습니다."),
    ;

    private final int code;
    private final String message;

}

