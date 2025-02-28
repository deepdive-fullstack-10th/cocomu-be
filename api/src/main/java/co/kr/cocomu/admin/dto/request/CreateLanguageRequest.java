package co.kr.cocomu.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "스터디 언어 추가 요청")
public record CreateLanguageRequest(
    @Schema(description = "언어 이름", example = "JAVA")
    @NotNull String languageName,
    @Schema(description = "언어 이미지", example = "https://cdn.cocomu.co.kr/images/languages/java.icon")
    @NotNull String languageImageUrl
) {
}