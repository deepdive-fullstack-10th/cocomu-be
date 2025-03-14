package co.kr.cocomu.dto.result;

public record SubmissionMessage(
    Long testCaseId,
    ExecutionMessage executionMessage
) {

    public static SubmissionMessage of(final Long testCaseId, final ExecutionMessage data) {
        return new SubmissionMessage(testCaseId, data);
    }

}
