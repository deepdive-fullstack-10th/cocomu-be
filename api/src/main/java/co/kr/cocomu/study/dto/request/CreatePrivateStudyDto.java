package co.kr.cocomu.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

@Schema(description = "비공개 스터디 생성 요청")
public record CreatePrivateStudyDto(
    @NotNull @Length(min = 2)
    @Schema(description = "스터디명", example = "매일 열심히하는 스터디")
    String name,
    @NotNull @Length(min = 4, max = 6)
    @Schema(description = "스터디 비밀번호", example = "1234")
    String password,
    @NotNull
    @Schema(description = "스터디에서 추구하는 언어 식별자", example = "[1, 2]")
    List<Long> languages,
    @NotNull
    @Schema(description = "스터디에서 추구하는 문제집 식별자", example = "[1, 2]")
    List<Long> workbooks,
    @Length(max = 255)
    @Schema(description = "스터디 설명", example = "우리 스터디는 ~ ")
    String description,
    @Min(2)
    @Schema(description = "스터디 최대 인원", example = "80")
    int totalUserCount
) {
}