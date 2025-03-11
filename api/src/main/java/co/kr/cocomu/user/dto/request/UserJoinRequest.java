package co.kr.cocomu.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 추가 요청")
public record UserJoinRequest(
    @Schema(description = "사용자 닉네임", example = "코코무")
    String nickname
) {
}
