package co.kr.cocomu.executor.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.producer.ExecutionRequestProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExecutorServiceTest {

    @Mock private ExecutionRequestProducer executionRequestProducer;
    @Mock private CodingSpaceDomainService codingSpaceDomainService;
    @InjectMocks private ExecutorService executorService;

    @Test
    void 코드_실행_서비스가_동작한다() {
        // when
        executorService.execute(mock(ExecuteDto.class));

        // then
        verify(codingSpaceDomainService).validateActiveTab(anyLong());
        verify(executionRequestProducer).publishExecution(any(ExecuteDto.class));
    }

}