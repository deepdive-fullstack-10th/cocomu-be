package co.kr.cocomu.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 상세 페이지 조회 응답")
public class StudyDetailPageDto {

    @Schema(description = "스터디명", example = "우리 스터디")
    private String name;
    @Schema(description = "스터디 사용 언어 정보")
    private List<LanguageDto> languages;
    @Schema(description = "코딩 스페이스 정보", example = "아직 미구현")
    private String codingSpaces;

}
