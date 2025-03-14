package co.kr.cocomu.service.impl;

import co.kr.cocomu.executor.container.NodeContainer;
import co.kr.cocomu.dto.receive.CodeExecutionMessage;
import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;
import co.kr.cocomu.service.CodeExecutor;
import co.kr.cocomu.service.DockerExecutor;
import co.kr.cocomu.executor.CodeFileManager;
import co.kr.cocomu.executor.TempDirectoryManager;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeCodeExecutor implements CodeExecutor {

    private final static String FILE_NAME = "Main.js";

    @Override
    public EventMessage<ExecutionMessage> execute(final CodeExecutionMessage message) {
        final String containerId = "node-exec-" + UUID.randomUUID();
        Path tempDir = null;

        try {
            tempDir = TempDirectoryManager.createTempDirectory();
            CodeFileManager.createFile(tempDir, FILE_NAME, message.code());
            final List<String> dockerCommand = NodeContainer.generate(containerId, tempDir, message.input().trim());

            return DockerExecutor.execute(dockerCommand, message.tabId(), tempDir);
        } catch (final Exception e) {
            return EventMessage.createErrorMessage(message.tabId(), e.getMessage());
        } finally {
            TempDirectoryManager.cleanup(tempDir);
        }
    }

}
