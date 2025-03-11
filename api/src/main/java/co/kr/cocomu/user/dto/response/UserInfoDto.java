package co.kr.cocomu.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 조회 응답")
public record UserInfoDto(
    @Schema(description = "사용자 식별자", example = "1")
    Long id,
    @Schema(description = "사용자 닉네임", example = "코코무")
    String nickname,
    @Schema(description = "사용자 프로필 이미지", example = "https://cdn.cocomu.co.kr/images/profile.png")
    String profileImageUrl,
    @Schema(description = "조회한 대상", example = "true")
    boolean me
) {}
