package co.kr.cocomu.executor.dto.message;

public record ExecutionMessage(
    Long tabId,
    String output,
    long executionTime,
    long memoryUsageKB
) {

    public static ExecutionMessage createRunningMessage(final Long tabId) {
        return new ExecutionMessage(tabId, "코드 실행 중...", 0, 0);
    }

}
