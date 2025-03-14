package co.kr.cocomu.executor.dto.message;

import co.kr.cocomu.executor.dto.request.SubmitDto;

public record CodeSubmissionMessage(
    Long testCaseId,
    CodeExecutionMessage codeExecutionMessage
) {

    public static CodeSubmissionMessage of(final Long testCaseId, final String input, final SubmitDto dto) {
        return new CodeSubmissionMessage(testCaseId, CodeExecutionMessage.of(dto, input));
    }

}
