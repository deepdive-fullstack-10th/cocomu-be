package co.kr.cocomu.service;

import co.kr.cocomu.dto.CodeExecutionMessage;
import co.kr.cocomu.dto.EventMessage;
import co.kr.cocomu.dto.ExecutionMessage;

public interface CodeExecutor {

    EventMessage<ExecutionMessage> execute(CodeExecutionMessage message);

}
