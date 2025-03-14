package co.kr.cocomu.executor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.TestCase;
import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import co.kr.cocomu.executor.producer.ExecutionRequestProducer;
import java.util.List;
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

    @Test
    void 코드_제출_서비스가_동작한다() {
        // Given
        CodingSpace mockSpace = mock(CodingSpace.class);
        when(codingSpaceDomainService.getCodingSpaceWithThrow(anyLong())).thenReturn(mockSpace);
        TestCase mockTestCase = mock(TestCase.class);
        when(mockSpace.getTestCases()).thenReturn(List.of(mockTestCase));
        when(mockTestCase.getInput()).thenReturn("");

        // when
        executorService.submit(mock(SubmitDto.class));

        // then
        verify(codingSpaceDomainService).validateActiveTab(anyLong());
        verify(executionRequestProducer).publishSubmission(anyLong(), anyString(), any(SubmitDto.class));
    }

    @Test
    void 코드_제출_결과가_정답인지_확인한다() {
        // Given
        TestCase mockTestCase = mock(TestCase.class);
        when(codingSpaceDomainService.getTestCaseWithThrow(anyLong())).thenReturn(mockTestCase);
        when(mockTestCase.checkAnswer(anyString())).thenReturn(true);

        // when
        boolean result = executorService.checkAnswer(1L, "");

        // then
        assertThat(result).isTrue();
    }

}