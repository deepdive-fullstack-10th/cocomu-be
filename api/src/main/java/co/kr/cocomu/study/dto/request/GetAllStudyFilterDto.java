package co.kr.cocomu.study.dto.request;

import co.kr.cocomu.study.domain.vo.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.security.PublicKey;
import java.util.List;

@Schema(description = "전체 스터디 조회 요청")
public record GetAllStudyFilterDto(
    @Schema(description = "스터디 공개 여부 필터", example = "PUBLIC", requiredMode = RequiredMode.NOT_REQUIRED)
    StudyStatus status,
    @Schema(description = "스터디 언어 필터", example = "\"1,2\"", requiredMode = RequiredMode.NOT_REQUIRED)
    List<Long> languages,
    @Schema(description = "스터디 문제집 필터", example = "\"1,2\"", requiredMode = RequiredMode.NOT_REQUIRED)
    List<Long> workbooks,
    @Schema(description = "스터디 참여 여부 필터", example = "true", requiredMode = RequiredMode.NOT_REQUIRED)
    Boolean joinable,
    @Schema(description = "스터디 제목 검색 필터", example = "스터디", requiredMode = RequiredMode.NOT_REQUIRED)
    String keyword,
    @Schema(description = "스터디 조회 페이지", example = "1", requiredMode = RequiredMode.NOT_REQUIRED)
    Long page
) {

    public static GetAllStudyFilterDto filterNothing() {
        return new GetAllStudyFilterDto(null, null, null, null, null, null);
    }

}
