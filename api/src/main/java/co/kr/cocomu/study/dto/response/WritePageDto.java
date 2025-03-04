package co.kr.cocomu.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "스터디 생성 페이지 응답")
public record WritePageDto(
    @Schema(description = "스터디 전체 문제집 정보")
    List<WorkbookDto> workbooks,
    @Schema(description = "스터디 전체 언어 정보")
    List<LanguageDto> languages
) {
}
