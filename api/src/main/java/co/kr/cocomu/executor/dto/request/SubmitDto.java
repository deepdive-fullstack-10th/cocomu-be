package co.kr.cocomu.executor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "코드 제출 요청")
public record SubmitDto(
    @Schema(description = "코딩 스페이스 식별자", example = "1")
    @NotNull Long codingSpaceId,
    @Schema(description = "코딩 스페이스 탭 식별자", example = "1")
    @NotNull Long codingSpaceTabId,
    @Schema(description = "코드 실행 언어", example = "java")
    @NotNull String language,
    @Schema(description = "코드", example = "println();")
    @NotNull String code
) {}
