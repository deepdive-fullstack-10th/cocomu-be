package co.kr.cocomu.executor.dto.message;

import co.kr.cocomu.executor.dto.request.SubmitDto;

public record CodeSubmissionMessage(
    Long testCaseId,
    CodeExecutionMessage codeExecutionMessage
) {

    public static CodeSubmissionMessage of(final Long testCaseId, final CodeExecutionMessage codeExecutionMessage) {
        return new CodeSubmissionMessage(testCaseId, codeExecutionMessage);
    }

}
