package co.kr.cocomu.service;

import co.kr.cocomu.dto.receive.CodeExecutionMessage;
import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;
import co.kr.cocomu.service.impl.CppCodeExecutor;
import co.kr.cocomu.service.impl.JavaCodeExecutor;
import co.kr.cocomu.service.impl.NodeCodeExecutor;
import co.kr.cocomu.service.impl.PythonCodeExecutor;
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
        executors.put("cpp", new CppCodeExecutor());
        executors.put("javascript", new NodeCodeExecutor());
        executors.put("python", new PythonCodeExecutor());
    }

    public EventMessage<ExecutionMessage> execute(final CodeExecutionMessage message) {
        final CodeExecutor executor = executors.get(message.language().toLowerCase());
        if (executor == null) {
            return EventMessage.createUnsupportedMessage(message.tabId());
        }
        return executor.execute(message);
    }

}
