package co.kr.cocomu.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 문제집 추가 응답")
public record LanguageResponse(
    @Schema(description = "언어 ID", example = "1")
    Long languageId,
    @Schema(description = "언어명", example = "JAVA")
    String languageName,
    @Schema(description = "언어 이미지 주소", example = "\"https://cdn.cocomu.co.kr/images/languages/java.icon")
    String languageImageUrl
) {
}
