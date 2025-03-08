package co.kr.cocomu.consumer;

public record CodeExecutionMessage(
    Long tabId,
    String language,
    String code,
    String input
) {}