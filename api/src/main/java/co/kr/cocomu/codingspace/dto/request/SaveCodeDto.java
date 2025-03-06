package co.kr.cocomu.codingspace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "최종 코드 저장 요청")
public record SaveCodeDto(
    @Schema(description = "최종 코드", example = "a = 10")
    @NotNull String code
) {
}
