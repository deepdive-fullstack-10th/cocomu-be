package co.kr.cocomu.dto;

public record CodeExecutionMessage(
    Long tabId,
    String language,
    String code,
    String input
) {}