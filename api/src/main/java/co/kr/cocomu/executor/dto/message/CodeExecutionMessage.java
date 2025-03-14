package co.kr.cocomu.executor.dto.message;

import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;

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

    public static CodeExecutionMessage of(final SubmitDto dto, final String input) {
        return new CodeExecutionMessage(
            dto.codingSpaceTabId(),
            dto.language(),
            dto.code(),
            input
        );
    }

}