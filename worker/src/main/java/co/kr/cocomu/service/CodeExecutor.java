package co.kr.cocomu.service;

import co.kr.cocomu.dto.receive.CodeExecutionMessage;
import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;

public interface CodeExecutor {

    EventMessage<ExecutionMessage> execute(CodeExecutionMessage message);

}
