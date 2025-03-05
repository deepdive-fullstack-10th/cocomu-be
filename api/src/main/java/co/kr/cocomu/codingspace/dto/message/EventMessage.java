package co.kr.cocomu.codingspace.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EventMessage<T> {

    private EventType type;
    private T data;

}
