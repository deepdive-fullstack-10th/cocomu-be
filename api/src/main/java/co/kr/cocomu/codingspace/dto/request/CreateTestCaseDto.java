package co.kr.cocomu.codingspace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "문제 테스트 케이스 요청")
public record CreateTestCaseDto(
    @NotNull @Schema(description = "입력 값", example = "3\n0\n1\n3")
    String input,
    @NotNull @Schema(description = "출력 값", example = "1 0\n0 1\n1 2")
    String output
) {
}
