package co.kr.cocomu.common.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 응답")
public record Api<T>(
    @Schema(description = "응답 코드", example = "9999")
    int code,
    @Schema(description = "응답 코드", example = "응답 성공 메시지입니다.")
    String message,
    @Schema(description = "응답 결과")
    T result
) {

    public static <T> Api<T> of(final ApiCode apiCode, final T result) {
        return new Api<>(apiCode.getCode(), apiCode.getMessage(), result);
    }

}