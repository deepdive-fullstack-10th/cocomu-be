package co.kr.cocomu.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "스터디 리더 정보 응답")
public class LeaderDto {

    @Schema(description = "스터디장 식별자", example = "1")
    private Long id;
    @Schema(description = "스터디장 닉네임", example = "코코무")
    private String nickname;
    @Schema(description = "스터디장 프로필 이미지", example = "https://cdn.cocomu.co.kr/images/...")
    private String profileImageUrl;

}
