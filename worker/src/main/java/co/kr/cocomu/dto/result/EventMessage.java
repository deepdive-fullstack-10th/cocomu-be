package co.kr.cocomu.dto.result;

import static co.kr.cocomu.dto.result.EventType.COMPILE_ERROR;
import static co.kr.cocomu.dto.result.EventType.RUNTIME_ERROR;
import static co.kr.cocomu.dto.result.EventType.SUCCESS;
import static co.kr.cocomu.dto.result.EventType.TIMEOUT_ERROR;
import static co.kr.cocomu.dto.result.EventType.UNKNOWN_ERROR;
import static co.kr.cocomu.dto.result.EventType.UNSUPPORTED_LANGUAGE;

import co.kr.cocomu.dto.ParseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EventMessage<T> {

    private EventType type;
    private T data;

    public static EventMessage<ExecutionMessage> createUnsupportedMessage(final Long tabId) {
        final ExecutionMessage message = new ExecutionMessage(tabId, "지원하지 않는 언어입니다", 0, 0);
        return new EventMessage<>(UNSUPPORTED_LANGUAGE, message);
    }

    public static EventMessage<ExecutionMessage> createTimeOutError(final Long tabId) {
        final ExecutionMessage message = new ExecutionMessage(tabId, "실행 시간이 초과되었습니다.", 0, 0);
        return new EventMessage<>(TIMEOUT_ERROR, message);
    }

    public static EventMessage<ExecutionMessage> createSuccessMessage(final Long tabId, final ParseResult res) {
        final ExecutionMessage message = new ExecutionMessage(tabId, res.output(), res.time(), res.memory());
        return new EventMessage<>(SUCCESS, message);
    }

    public static EventMessage<ExecutionMessage> createErrorMessage(final Long tabId, final String error) {
        final ExecutionMessage message = new ExecutionMessage(tabId, error, 0, 0);
        return new EventMessage<>(UNKNOWN_ERROR, message);
    }

    public static EventMessage<ExecutionMessage> createRuntimeError(final Long tabId, final String output) {
        final ExecutionMessage message = new ExecutionMessage(tabId, output, 0, 0);
        return new EventMessage<>(RUNTIME_ERROR, message);
    }

    public static EventMessage<ExecutionMessage> createCompileError(final Long tabId, final String output) {
        final ExecutionMessage message = new ExecutionMessage(tabId, output, 0, 0);
        return new EventMessage<>(COMPILE_ERROR, message);
    }

    public static EventMessage<SubmissionMessage> createSubmissionMessage(
        final EventType type,
        final SubmissionMessage submissionMessage
    ) {
        return new EventMessage<>(type, submissionMessage);
    }

}
