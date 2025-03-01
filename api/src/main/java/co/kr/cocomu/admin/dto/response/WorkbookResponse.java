package co.kr.cocomu.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 문제집 추가 응답")
public record WorkbookResponse(
    @Schema(description = "문제집 ID", example = "1")
    Long workbookId,
    @Schema(description = "문제집 명", example = "백준")
    String workbookName,
    @Schema(description = "문제집 이미지 주소", example = "https://cdn.cocomu.co.kr/images/workbooks/boj.icon")
    String workbookImageUrl
) {
}
