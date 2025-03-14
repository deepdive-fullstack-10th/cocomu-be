package co.kr.cocomu.executor.dto.message;

public record EventMessage<T>(
    EventType type,
    T data
) { }
