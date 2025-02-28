package co.kr.cocomu.common.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 응답")
public record NoContent(
    @Schema(description = "응답 코드", example = "9999")
    int code,
    @Schema(description = "응답 코드", example = "응답 성공 메시지입니다.")
    String message
) {

    public static NoContent from(final ApiCode apiCode) {
        return new NoContent(apiCode.getCode(), apiCode.getMessage());
    }

}
