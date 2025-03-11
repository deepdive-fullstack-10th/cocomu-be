package co.kr.cocomu.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "스터디 수정 요청")
public record EditStudyDto(
    @Schema(description = "스터디 명", example = "코테 스터디")
    String name,
    @Schema(description = "스터디 설명", example = "코테 준비하는 스터디")
    String description,
    @NotNull
    @Schema(description = "공개 스터디 여부")
    boolean publicStudy,
    @Schema(description = "스터디 암호", example = "1234")
    String password,
    @NotNull
    @Schema(description = "스터디 최대 인원 수", example = "10")
    int totalUserCount,
    @NotNull
    @Schema(description = "스터디에서 추구하는 언어 식별자", example = "[1, 2]")
    List<Long> languages,
    @NotNull
    @Schema(description = "스터디에서 추구하는 문제집 식별자", example = "[1, 2]")
    List<Long> workbooks
) {
}
