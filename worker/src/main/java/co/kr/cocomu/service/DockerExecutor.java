package co.kr.cocomu.service;

import static co.kr.cocomu.executor.docker.DockerConstant.COMPILE_ERROR_EXIT_CODE;
import static co.kr.cocomu.executor.docker.DockerConstant.RUNTIME_ERROR_EXIT_CODE;
import static co.kr.cocomu.executor.docker.DockerConstant.SUCCESS_OUT_CODE;
import static co.kr.cocomu.executor.docker.DockerConstant.TIMEOUT_EXIT_CODE;
import static co.kr.cocomu.executor.CodeFileManager.readFile;

import co.kr.cocomu.executor.docker.DockerConstant;
import co.kr.cocomu.dto.result.EventMessage;
import co.kr.cocomu.dto.result.ExecutionMessage;
import co.kr.cocomu.dto.ParseResult;
import co.kr.cocomu.executor.OutputParser;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerExecutor {

    public static EventMessage<ExecutionMessage> execute(
        final List<String> command,
        final Long tabId,
        final Path tempDir
    ) throws IOException, InterruptedException {
        final ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        final Process process = pb.start();
        final int exitCode = process.waitFor();

        return switch (exitCode) {
            case COMPILE_ERROR_EXIT_CODE -> processCompileError(tabId, tempDir);
            case TIMEOUT_EXIT_CODE -> EventMessage.createTimeOutError(tabId);
            case RUNTIME_ERROR_EXIT_CODE -> processRuntimeError(tabId, tempDir);
            case SUCCESS_OUT_CODE -> processSuccess(tabId, tempDir);
            default -> throw new IllegalStateException("알 수 없는 오류가 발생했습니다 코드 번호: " + exitCode);
        };
    }

    private static EventMessage<ExecutionMessage> processCompileError(final Long tabId, final Path tempDir) {
        final String output = readFile(tempDir, DockerConstant.COMPILE_EXECUTION_LOG).trim();
        return EventMessage.createCompileError(tabId, output);
    }

    private static EventMessage<ExecutionMessage> processRuntimeError(final Long tabId, final Path tempDir) {
        final String output = readFile(tempDir, DockerConstant.EXECUTION_RESULT_LOG).trim();
        final ParseResult parseResult = OutputParser.parse(output);
        return EventMessage.createRuntimeError(tabId, parseResult.output());
    }

    private static EventMessage<ExecutionMessage> processSuccess(final Long tabId, final Path tempDir) {
        final String output = readFile(tempDir, DockerConstant.EXECUTION_RESULT_LOG).trim();
        log.info("======== low output =========\n{} ", output);
        final ParseResult parseResult = OutputParser.parse(output);
        return EventMessage.createSuccessMessage(tabId, parseResult);
    }

}
