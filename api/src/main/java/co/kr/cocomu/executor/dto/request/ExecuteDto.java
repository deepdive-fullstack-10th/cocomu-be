package co.kr.cocomu.executor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "코드 실행 요청")
public record ExecuteDto(
    @Schema(description = "코딩 스페이스 탭 식별자", example = "1")
    @NotNull Long codingSpaceTabId,
    @Schema(description = "코드 실행 언어", example = "java")
    @NotNull String language,
    @Schema(description = "코드", example = "println();")
    @NotNull String code,
    @Schema(description = "테스트 케이스 입력 데이터", example = "1 2")
    @NotNull String input
) {
}
