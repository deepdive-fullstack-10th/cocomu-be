package co.kr.cocomu.executor.controller;

import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.executor.controller.code.ExecutorApiCode;
import co.kr.cocomu.executor.controller.docs.ExecutorControllerDocs;
import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.service.CodeExecutionProducer;
import co.kr.cocomu.executor.service.StompSseProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/executor")
@Slf4j
public class ExecutorController implements ExecutorControllerDocs {

    private final CodeExecutionProducer codeExecutionProducer;
    private final CodingSpaceDomainService codingSpaceDomainService;
    private final StompSseProducer stompSseProducer;

    @PostMapping("/execution")
    public NoContent executeCode(@RequestBody final ExecuteDto dto) {
        codingSpaceDomainService.validateActiveTab(dto.codingSpaceTabId());
        codeExecutionProducer.publishCode(dto);
        stompSseProducer.publishRunning(dto.codingSpaceTabId());

        return NoContent.from(ExecutorApiCode.EXECUTE_CODE_SUCCESS);
    }

    @PostMapping("/result")
    public NoContent handleExecutionResult(@RequestBody final EventMessage<ExecutionMessage> result) {
        log.info("코드 실행결과 전달받기 {}", result.toString());
        stompSseProducer.publishResult(result);
        return NoContent.from(ExecutorApiCode.EXECUTE_SENT_SUCCESS);
    }

}
