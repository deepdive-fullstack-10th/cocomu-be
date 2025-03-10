package co.kr.cocomu.study.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyApiCode implements ApiCode {

    CREATE_STUDY_SUCCESS(3000, "스터디 생성에 성공했습니다."),
    JOIN_STUDY_SUCCESS(3000, "스터디 참여에 성공했습니다."),
    GET_ALL_STUDIES_SUCCESS(3000, "전체 스터디 조회에 성공했습니다."),
    GET_STUDY_INFO_SUCCESS(3000, "스터디 정보 조회에 성공했습니다."),
    GET_STUDY_PAGE_SUCCESS(3000, "스터디 페이지 조회에 성공했습니다."),
    GET_STUDY_DETAIL_SUCCESS(3000, "스터디 디테일 페이지 조회에 성공했습니다."),
    GET_FILTER_OPTIONS_SUCCESS(3000, "스터디 필터 옵션 조회에 성공했습니다."),
    LEAVE_STUDY_SUCCESS(3000, "스터디 나가기에 성공했습니다."),
    REMOVE_STUDY_SUCCESS(3000, "스터디 삭제에 성공했습니다."),
    GET_ALL_MEMBERS_SUCCESS(3000, "스터디 사용자 목록 조회에 성공했습니다."),
    EDIT_STUDY_SUCCESS(3000, "스터디 수정이 성공했습니다.");

    private final int code;
    private final String message;

    }
