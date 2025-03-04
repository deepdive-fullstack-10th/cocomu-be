package co.kr.cocomu.codingspace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "코딩 스페이스 생성 요청")
public record CreateCodingSpaceDto(
    @NotNull @Schema(description = "스터디 식별자", example = "1")
    Long studyId,
    @NotNull @Schema(description = "코딩 풀이 최대 인원", example = "4")
    int totalUserCount,
    @NotNull @Schema(description = "코딩 풀이 진행 시간", example = "30")
    int timerTime,
    @NotNull @Schema(description = "코딩시 진행할 언어 선택", example = "1")
    Long languageId,
    @Schema(description = "문제집 링크", example = "https://www.acmicpc.net/problem/1003")
    String workbookUrl,
    @NotNull @Schema(description = "코딩 스페이스 명", example = "[BOJ-1003, Silver3] 피보나치 함수")
    String name,
    @Schema(description = "문제 내용", example = "다음 소스는 N번째 피보나치 수를 구하는 C++ 함수이다 ...")
    String description,
    @Schema(description = "문제 가본 테스트 케이스")
    List<CreateTestCaseDto> testcases
) {
}
