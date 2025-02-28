package co.kr.cocomu.user.controller.docs;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.user.dto.response.UserDto;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "001. COCOMU USER", description = "코코무 사용자 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 기능")
    @ApiResponse(
        responseCode = "1000",
        description = "사용자 정보를 조회했습니다."
    )
    @ApiResponse(
        responseCode = "1001",
        description = "사용자를 찾을 수 없습니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<UserDto> getUser(Long userId);

    @Operation(summary = "모든 사용자 정보 조회", description = "모든 사용자 정보를 조회하는 기능")
    @ApiResponse(
        responseCode = "1000",
        description = "모든 사용자 정보를 조회했습니다."
    )
    Api<List<UserDto>> getUsers();

    @Operation(summary = "사용자 정보 추가", description = "사용자 정보를 추가하는 기능")
    @ApiResponse(
        responseCode = "1000",
        description = "사용자 정보가 추가되었습니다."
    )
    Api<UserDto> createUser(UserJoinRequest userId);

}
