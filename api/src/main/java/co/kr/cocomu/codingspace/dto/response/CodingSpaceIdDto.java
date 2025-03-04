package co.kr.cocomu.codingspace.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코딩 스페이스 생성 응답")
public record CodingSpaceIdDto(@Schema(description = "코딩 스페이스 ID", example = "1") Long codingSpaceId) {
}
