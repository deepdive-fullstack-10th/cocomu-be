package co.kr.cocomu.dto.receive;


public record CodeSubmissionMessage(
    Long testCaseId,
    CodeExecutionMessage codeExecutionMessage
) {
}
