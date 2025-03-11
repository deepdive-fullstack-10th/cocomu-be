package co.kr.cocomu.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 요청")
public record ProfileUpdateDto(
    @Schema(description = "사용자 닉네임", example = "코코무")
    String nickname,
    @Schema(description = "사용자 프로필 이미지", example = "https://cdn.cocomu.co.kr/images/...")
    String profileImageUrl
) {
}
