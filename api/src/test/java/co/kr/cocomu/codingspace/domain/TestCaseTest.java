package co.kr.cocomu.codingspace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.codingspace.domain.vo.TestCaseType;
import co.kr.cocomu.codingspace.dto.request.CreateTestCaseDto;
import org.junit.jupiter.api.Test;

class TestCaseTest {

    @Test
    void 테스트_케이스_생성이_된다() {
        // given
        CreateTestCaseDto dto = new CreateTestCaseDto("input", "output");

        // when
        TestCase result = TestCase.createDefaultCase(dto);

        // then
        assertThat(result.getInput()).isEqualTo("input");
        assertThat(result.getOutput()).isEqualTo("output");
    }

    @Test
    void 기본_테스트_케이스_생성이_된다() {
        // given
        CreateTestCaseDto dto = new CreateTestCaseDto("input", "output");

        // when
        TestCase result = TestCase.createDefaultCase(dto);

        // then
        assertThat(result.getType()).isEqualTo(TestCaseType.DEFAULT);
    }

    @Test
    void 커스텀_테스트_케이스_생성이_된다() {
        // given
        CreateTestCaseDto dto = new CreateTestCaseDto("input", "output");

        // when
        TestCase result = TestCase.createCustomCase(dto);

        // then
        assertThat(result.getType()).isEqualTo(TestCaseType.CUSTOM);
    }

}