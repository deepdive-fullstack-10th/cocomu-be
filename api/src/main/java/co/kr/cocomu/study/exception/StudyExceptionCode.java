package co.kr.cocomu.study.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyExceptionCode implements ExceptionCode {

    NOT_FOUND_STUDY(3001, "존재하지 않는 스터디입니다."),
    ALREADY_PARTICIPATION_STUDY(3002, "이미 스터디에 참여되었습니다."),
    LEADER_MUST_USE_REMOVE(3003, "스터디장은 스터디 삭제를 이용해주세요."),
    ONLY_LEADER_CAN_REMOVE_STUDY(3004, "스터디장만 스터디를 삭제 할 수 있습니다."),
    REMAINING_USER(3005, "스터디원이 남아있으면 스터디를 제거할 수 없습니다."),
    NO_PARTICIPATION_USER(3006, "해당 스터디의 스터디원이 아닙니다.");

    private final int code;
    private final String message;

}
