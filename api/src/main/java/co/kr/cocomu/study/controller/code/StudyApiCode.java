package co.kr.cocomu.study.controller.code;

import co.kr.cocomu.common.api.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyApiCode implements ApiCode {

    CREATE_STUDY_SUCCESS(3000, "스터디 생성에 성공했습니다."),
    JOIN_STUDY_SUCCESS(3000, "스터디 참여에 성공했습니다.");

    private final int code;
    private final String message;

}
