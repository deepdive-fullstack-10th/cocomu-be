package co.kr.cocomu.codingspace.dto.response;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "코딩 스페이스 미리보기 조회 응답")
public class CodingSpaceDto {

    @Schema(description = "코딩 스페이스 id", example = "1")
    private Long id;
    @Schema(description = "코딩 스페이스 참여 여부", example = "true")
    private boolean joinedMe;
    @Schema(description = "코딩 스페이스명", example = "[BOJ, Silver] 1130")
    private String name;
    @Schema(description = "코딩 스페이스 언어")
    private LanguageDto language;
    @Schema(description = "코딩 스페이스 현재 인원 수", example = "1")
    private int currentUserCount;
    @Schema(description = "코딩 스페이스 최대 인원 수", example = "2")
    private int totalUserCount;
    @Schema(description = "코딩 스페이스 생성 시간")
    private LocalDateTime createdAt;
    @Schema(description = "코딩 스페이스 상태", example = "WAITING")
    private CodingSpaceStatus status;
    @Schema(description = "코딩 스페이스 참여 인원")
    private List<UserDto> currentUsers;

}
