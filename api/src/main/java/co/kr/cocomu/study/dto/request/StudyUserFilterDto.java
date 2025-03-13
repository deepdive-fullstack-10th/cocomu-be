package co.kr.cocomu.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 사용자 목록 조회 Filter")
public record StudyUserFilterDto(
    @Schema(description = "마지막 조회한 사용자 닉네임", example = "코코무")
    String lastNickname,
    @Schema(description = "마지막 조회한 스터디 사용자 식별자", example = "1")
    Long lastIndex
) {
}
