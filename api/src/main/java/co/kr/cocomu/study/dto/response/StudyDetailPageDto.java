package co.kr.cocomu.study.dto.response;

import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.study.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Schema(description = "스터디 상세 페이지 조회 응답")
public class StudyDetailPageDto {

    @Schema(description = "스터디명", example = "우리 스터디")
    private String name;
    @Schema(description = "스터디 사용 언어 정보")
    private List<LanguageDto> languages;
    @Schema(description = "코딩 스페이스 정보", example = "아직 미구현")
    private List<CodingSpaceDto> codingSpaces;
    @Schema(description = "코딩 스페이스 마지막 조회 식별자", example = "10")
    private Long lastId;

    public static StudyDetailPageDto of(final Study study, final CodingSpacesDto codingSpacesDto) {
        return StudyDetailPageDto.builder()
            .name(study.getName())
            .codingSpaces(codingSpacesDto.codingSpaces())
            .lastId(codingSpacesDto.lastId())
            .build();
    }

}
