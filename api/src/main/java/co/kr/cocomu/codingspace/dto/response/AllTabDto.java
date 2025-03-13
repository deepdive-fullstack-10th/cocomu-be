package co.kr.cocomu.codingspace.dto.response;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "코딩 스페이스 전체 탭 정보 응답")
public class AllTabDto {

    @Schema(description = "코딩 스페이스 탭 식별자", example = "1")
    private Long tabId;
    @Schema(description = "코딩 스페이스 탭 문서 키", example = "fedt3fdf-33f2dfsa-dfd57afs-a6s5d3fa")
    private String documentKey;
    @Schema(description = "코딩 스페이스 멤버 식별자", example = "1")
    private Long userId;
    @Schema(description = "코딩 스페이스 멤버 닉네임", example = "코코무")
    private String nickname;
    @Schema(description = "코딩 스페이스 멤버 프로필 이미지", example = "https://cdn.cocomu.co.kr/images/...")
    private String profileImageUrl;
    @Schema(description = "자기 탭인지")
    private boolean myTab;
    @Schema(description = "코딩 스페이스 권한", example = "HOST")
    private CodingSpaceRole role;

}
