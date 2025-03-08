package co.kr.cocomu.service;

import co.kr.cocomu.dto.CodeExecutionMessage;
import co.kr.cocomu.dto.EventMessage;
import co.kr.cocomu.dto.ExecutionMessage;
import co.kr.cocomu.service.impl.JavaCodeExecutor;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionService {

    private final Map<String, CodeExecutor> executors = new HashMap<>();

    public CodeExecutionService() {
        executors.put("java", new JavaCodeExecutor());
    }

    public EventMessage<ExecutionMessage> execute(final CodeExecutionMessage message) {
        final CodeExecutor executor = executors.get(message.language().toLowerCase());
        if (executor == null) {
            return EventMessage.createUnsupportedMessage(message.tabId());
        }
        return executor.execute(message);
    }

}
