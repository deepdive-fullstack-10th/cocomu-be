package co.kr.cocomu.study.dto.request;

import co.kr.cocomu.study.domain.vo.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 스터디 조회 요청")
public record GetAllStudyFilterDto(
    @Schema(description = "스터디 공개 여부 필터", example = "PUBLIC")
    StudyStatus status,
    @Schema(description = "스터디 언어 필터", example = "\"1,2\"")
    List<Long> languages,
    @Schema(description = "스터디 문제집 필터", example = "\"1,2\"")
    List<Long> workbooks,
    @Schema(description = "스터디 참여 여부 필터", example = "true")
    Boolean joinable,
    @Schema(description = "스터디 제목 검색 필터", example = "스터디")
    String keyword,
    @Schema(description = "스터디 조회 페이지", example = "1")
    Long page
) {

    public static GetAllStudyFilterDto filterNothing() {
        return new GetAllStudyFilterDto(null, null, null, null, null, null);
    }

}
