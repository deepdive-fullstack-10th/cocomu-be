package co.kr.cocomu.codingspace.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodingSpaceApiCode implements ApiCode {

    CREATE_CODING_SPACE_SUCCESS(4000, "코딩 스페이스 생성에 성공했습니다."),
    JOIN_CODING_SPACE_SUCCESS(4000, "코딩 스페이스 참여에 성공했습니다."),
    GET_WRITE_PAGE_SUCCESS(4000, "코딩 스페이스 생성 페이지 조회에 성공했습니다."),
    GET_CODING_SPACES_SUCCESS(4000, "코딩 스페이스 목록 조회에 성공했습니다."),
    ENTER_WAITING_SPACE_SUCCESS(4000, "코딩 스페이스 대기방 입장에 성공했습니다.");

    private final int code;
    private final String message;

}
