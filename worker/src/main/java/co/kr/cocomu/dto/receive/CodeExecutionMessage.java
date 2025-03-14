package co.kr.cocomu.dto.receive;

public record CodeExecutionMessage(
    Long tabId,
    String language,
    String code,
    String input
) {}