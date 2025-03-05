package co.kr.cocomu.codingspace.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventMessage<T> {

    private EventType type;
    private T data;

}
