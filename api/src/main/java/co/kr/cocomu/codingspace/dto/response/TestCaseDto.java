package co.kr.cocomu.codingspace.dto.response;

import co.kr.cocomu.codingspace.domain.TestCase;
import co.kr.cocomu.codingspace.domain.vo.TestCaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestCaseDto {

    private Long testCaseId;
    private String input;
    private String output;
    private TestCaseType type;

}
