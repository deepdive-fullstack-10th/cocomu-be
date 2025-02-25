package co.kr.cocomu.user.dto.response;

import co.kr.cocomu.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "모든 사용자 조회 응답")
public record AllUserResponse(
    @Schema(description = "모든 사용자 정보")
    List<UserDto> users
) {
}
