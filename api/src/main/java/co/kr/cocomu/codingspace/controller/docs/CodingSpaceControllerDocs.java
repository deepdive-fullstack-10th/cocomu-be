package co.kr.cocomu.codingspace.controller.docs;

import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceTabIdDto;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "004. COCOMU-CODING-SPACE", description = "코코무 코딩 스페이스 관련 API")
public interface CodingSpaceControllerDocs {

    @Operation(summary = "코딩 스페이스 생성", description = "코딩 스페이스를 생성하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 생성에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
                코딩 스페이스의 최대 인원은 4명입니다.
                코딩 스페이스의 최소 인원은 2명입니다.
                해당 스터디에서 사용하지 않는 언어입니다.
                해당 스터디의 스터디원이 아닙니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            존재하지 않는 스터디입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<CodingSpaceIdDto> createCodingSpace(CreateCodingSpaceDto dto, Long userId);


    @Operation(summary = "코딩 스페이스 참여", description = "코딩 스페이스에 참여하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 참여에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
                해당 스터디의 스터디원이 아닙니다.
                이미 코딩 스페이스에 참여되었습니다.
                대기중인 코딩 스페이가 아닙니다.
                코딩 스페이스 최대 인원을 초과헀습니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            존재하지 않는 스터디입니다.
            사용자를 찾을 수 없습니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<CodingSpaceTabIdDto> joinCodingSpace(Long codingSpaceId, Long userId);

}
