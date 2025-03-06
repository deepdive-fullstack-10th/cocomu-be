package co.kr.cocomu.codingspace.dto.page;

import co.kr.cocomu.codingspace.dto.response.AllTabDto;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "코딩 스페이스 피드백 페이지 응답")
public class FeedbackPage {

    @Schema(description = "코딩 스페이스 식별자", example = "1")
    private Long id;
    @Schema(description = "코딩 스페이스 명", example = "[boj, silver3] ...")
    private String name;
    @Schema(description = "코딩 스페이스 내용", example = "이 문제는 ...")
    private String description;
    @Schema(description = "코딩 스페이스 문제집 링크", example = "https://acmicpc.net/~~")
    private String workbookUrl;
    @Schema(description = "코딩 스페이스 방장 여부", example = "true")
    private boolean hostMe;
    @Schema(description = "코딩 스페이스 진행 시간(분)", example = "30")
    private int codingMinutes;
    @Schema(description = "코딩 스페이스 시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "코딩 스페이스 종료 시간")
    private LocalDateTime finishTime;

    @Schema(description = "코딩 스페이스 언어 정보")
    private LanguageDto language;
    @Schema(description = "코딩 스페이스 활성화 된 탭 정보")
    private List<AllTabDto> activeTabs;
    @Schema(description = "코딩 스페이스 테스트 케이스 정보")
    private List<TestCaseDto> testCases;

}