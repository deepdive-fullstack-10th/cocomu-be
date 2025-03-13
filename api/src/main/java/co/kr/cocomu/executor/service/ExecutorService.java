package co.kr.cocomu.executor.service;

import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.producer.CodeExecutionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutorService {

    private final CodeExecutionProducer codeExecutionProducer;
    private final CodingSpaceDomainService codingSpaceDomainService;

    public void execute(final ExecuteDto dto) {
        codingSpaceDomainService.validateActiveTab(dto.codingSpaceTabId());
        codeExecutionProducer.publishCode(dto);
    }

}
