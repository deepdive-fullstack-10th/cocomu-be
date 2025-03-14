package co.kr.cocomu.executor.dto.message;

public record SubmissionMessage(
    Long testCaseId,
    ExecutionMessage executionMessage
) {
}
