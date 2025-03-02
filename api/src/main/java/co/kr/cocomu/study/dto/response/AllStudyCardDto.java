package co.kr.cocomu.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 스터디 동적 쿼리 조회")
public record AllStudyCardDto(
    @Schema(description = "필터링 된 전체 스터디 수", example = "100")
    Long totalStudyCount,
    @Schema(description = "조회 스터디 결과")
    List<StudyCardDto> studies
) {
}
