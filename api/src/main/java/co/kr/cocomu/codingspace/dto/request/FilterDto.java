package co.kr.cocomu.codingspace.dto.request;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "코딩 스페이스 조회 필터 요청")
public record FilterDto(
    @Schema(description = "코딩 스페이스 상태", example = "WAITING")
    CodingSpaceStatus status,
    @Schema(description = "언어 식별자", example = "1,2")
    List<Long> languageIds,
    @Schema(description = "참여 여부", example = "true")
    Boolean joinable,
    @Schema(description = "코딩 스페이스 제목", example = "BOJ")
    String keyword,
    @Schema(description = "마지막 조회 ID", example = "100")
    Long lastId
) {
}
