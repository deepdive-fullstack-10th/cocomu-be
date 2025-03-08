package co.kr.cocomu.codingspace.dto.page;

import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "코딩 스페이스 생성 페이지 응답")
public record StudyInformationPage(
    @Schema(description = "코딩 스페이스 사용 언어 목록")
    List<LanguageDto> languages
) {
}
