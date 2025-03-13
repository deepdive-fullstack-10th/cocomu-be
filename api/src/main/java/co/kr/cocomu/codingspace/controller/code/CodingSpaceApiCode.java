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
    ENTER_SPACE_SUCCESS(4000, "코딩 스페이스 입장에 성공했습니다."),
    GET_WAITING_PAGE_SUCCESS(4000, "코딩 스페이스 대기방 조회에 성공했습니다."),
    START_CODING_SPACE(4000, "코딩 스페이스 시작이 성공했습니다."),
    GET_STARTING_PAGE_SUCCESS(4000, "코딩 스페이스 시작 페이지 조회에 성공했습니다."),
    START_FEEDBACK_MODE(4000, "피드백 모드가 시작되었습니다."),
    FINISH_SPACE_SUCCESS(4000, "코딩 스페이스 종료가 성공했습니다."),
    SAVE_FINAL_CODE_SUCCESS(4000, "최종 코드 저장이 성공했습니다."),
    GET_FINISH_PAGE_SUCCESS(4000, "코딩 스페이스 종료 페이지 조회에 성공했습니다."),
    GET_FEEDBACK_PAGE_SUCCESS(4000, "코딩 스페이스 피드백 페이지 조회에 성공했습니다."),
    ADD_TEST_CASE_SUCCESS(4000, "코딩 스페이스 테스트 케이스 추가에 성공했습니다."),
    DELETE_TEST_CASE_SUCCESS(4000, "테스트 케이스 삭제에 성공했습니다."),
    DELETE_SPACE_SUCCESS(4000, "코딩 스페이스 삭제에 성공했습니다.");

    private final int code;
    private final String message;

}
