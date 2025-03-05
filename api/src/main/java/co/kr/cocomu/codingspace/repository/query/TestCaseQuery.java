package co.kr.cocomu.codingspace.repository.query;

import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
import java.util.List;

public interface TestCaseQuery {

    List<TestCaseDto> findTestCases(Long codingSpaceId);

}
