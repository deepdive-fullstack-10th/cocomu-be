package co.kr.cocomu.codingspace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코딩 스페이스 조회 필터 요청")
public record FilterDto(
    @Schema(description = "마지막 조회 ID")
    Long lastId
) {
}
