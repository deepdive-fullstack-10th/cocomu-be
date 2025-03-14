package co.kr.cocomu.dto.result;

public record ExecutionMessage(
    Long tabId,
    String output,
    long executionTime,
    long memoryUsageKB
) {
}
