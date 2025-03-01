package co.kr.cocomu.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "스터디 문제집 추가 요청")
public record CreateWorkbookRequest(
    @Schema(description = "문제집 이름", example = "백준")
    @NotNull String workbookName,
    @Schema(description = "문제집 이미지", example = "https://cdn.cocomu.co.kr/images/workbooks/boj.icon")
    @NotNull String workbookImageUrl
) {
}
