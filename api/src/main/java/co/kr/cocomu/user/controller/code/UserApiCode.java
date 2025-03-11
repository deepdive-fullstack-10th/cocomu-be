package co.kr.cocomu.user.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserApiCode implements ApiCode {

    USER_JOIN_SUCCESS(1000, "사용자 정보가 추가되었습니다."),
    GET_USER_INFO_SUCCESS(1000, "사용자 정보 조회에 성공했습니다."),
    ALL_USER_FOUND_SUCCESS(1000, "모든 사용자 정보를 조회했습니다."),
    PROFILE_UPDATE_SUCCESS(1000, "프로필 정보 수정이 성공했습니다."),
    PROFILE_UPLOAD_SUCCESS(1000, "프로필 이미지 업로드가 성공했습니다."),
    GET_STUDIES_SUCCESS(1000, "참여한 스터디 목록 조회에 성공했습니다."),
    GET_SPACES_SUCCESS(1000, "참여한 코딩 스페이스 목록 조회에 성공했습니다.");

    private final int code;
    private final String message;

}
