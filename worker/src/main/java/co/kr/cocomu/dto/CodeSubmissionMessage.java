package co.kr.cocomu.dto;


public record CodeSubmissionMessage(
    Long testCaseId,
    CodeExecutionMessage codeExecutionMessage
) {
}
