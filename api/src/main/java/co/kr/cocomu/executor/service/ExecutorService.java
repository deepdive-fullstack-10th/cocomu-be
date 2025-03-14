package co.kr.cocomu.executor.service;

import co.kr.cocomu.codingspace.domain.TestCase;
import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import co.kr.cocomu.executor.producer.ExecutionRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExecutorService {

    private final ExecutionRequestProducer executionRequestProducer;
    private final CodingSpaceDomainService codingSpaceDomainService;

    public void execute(final ExecuteDto dto) {
        codingSpaceDomainService.validateActiveTab(dto.codingSpaceTabId());
        executionRequestProducer.publishExecution(dto);
    }

    public void submit(final SubmitDto dto) {
        codingSpaceDomainService.validateActiveTab(dto.codingSpaceTabId());
        codingSpaceDomainService.getCodingSpaceWithThrow(dto.codingSpaceId())
            .getTestCases()
            .forEach(testCase -> {
                final Long testCaseId = testCase.getId();
                final String input = testCase.getInput();
                executionRequestProducer.publishSubmission(testCaseId, input, dto);
            });
    }

    public boolean checkAnswer(final Long testCaseId, final String output) {
        final TestCase testCase = codingSpaceDomainService.getTestCaseWithThrow(testCaseId);
        return testCase.checkAnswer(output);
    }

}
