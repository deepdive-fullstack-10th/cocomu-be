package co.kr.cocomu.codingspace.dto.response;

import co.kr.cocomu.study.domain.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 언어 응답")
public class LanguageDto {

    @Schema(description = "스터디 언어 식별자", example = "1")
    private Long languageId;
    @Schema(description = "스터디 언어명", example = "JAVA")
    private String languageName;
    @Schema(description = "스터디 언어 ICON", example = "https://cdn.cocomu.co.kr/...")
    private String languageImageUrl;

    public static LanguageDto from(final Language language) {
        return new LanguageDto(language.getId(), language.getName(), language.getImageUrl());
    }

}
