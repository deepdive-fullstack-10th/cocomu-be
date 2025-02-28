package co.kr.cocomu.study.dto.request;

import java.util.List;

public record CreatePublicStudyDto(
    String name,
    List<Integer> languages,
    List<Integer> judges,
    String description,
    int totalUserCount
) {
}
