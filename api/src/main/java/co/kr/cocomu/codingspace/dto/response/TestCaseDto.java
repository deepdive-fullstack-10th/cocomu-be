package co.kr.cocomu.codingspace.dto.response;

import co.kr.cocomu.codingspace.domain.TestCase;
import co.kr.cocomu.codingspace.domain.vo.TestCaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(description = "테스트 케이스 응답")
public class TestCaseDto {

    @Schema(description = "테스트 케이스 식별자", example = "1")
    private Long testCaseId;
    @Schema(description = "테스트 케이스 입력 값", example = "1 2")
    private String input;
    @Schema(description = "테스트 케이스 출력 값", example = "3")
    private String output;
    @Schema(description = "테스트 케이스 종류", example = "1")
    private TestCaseType type;

    public static TestCaseDto from(final TestCase customTestCase) {
        return new TestCaseDto(
            customTestCase.getId(),
            customTestCase.getInput(),
            customTestCase.getOutput(),
            customTestCase.getType()
        );
    }

}
