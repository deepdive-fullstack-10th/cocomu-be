package co.kr.cocomu.study.dto.request;

import java.util.List;

public record EditStudyDto(
    String name,
    String description,
    boolean publicStudy,
    String password,
    int totalUserCount,
    List<Long> languages,
    List<Long> workbooks
) {
}
