package co.kr.cocomu.executor.dto.message;

import co.kr.cocomu.executor.dto.request.ExecuteDto;

public record CodeExecutionMessage(
    Long tabId,
    String language,
    String code,
    String input
) {

    public static CodeExecutionMessage createNewMessage(final ExecuteDto dto) {
        return new CodeExecutionMessage(
            dto.codingSpaceTabId(),
            dto.language(),
            dto.code(),
            dto.input()
        );
    }

}