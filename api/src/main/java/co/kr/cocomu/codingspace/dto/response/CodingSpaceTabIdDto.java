package co.kr.cocomu.codingspace.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코딩 스페이스 탭 생성 응답")
public record CodingSpaceTabIdDto(
    @Schema(description = "코딩 스페이스 탭 ID", example = "UUID")
    Long codingSpaceTabId
) {
}
