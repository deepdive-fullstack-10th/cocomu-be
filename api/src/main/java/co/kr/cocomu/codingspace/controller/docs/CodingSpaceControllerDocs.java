package co.kr.cocomu.codingspace.controller.docs;

import co.kr.cocomu.codingspace.dto.page.StartingPage;
import co.kr.cocomu.codingspace.dto.page.WaitingPage;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceTabIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.codingspace.dto.page.WritePage;
import co.kr.cocomu.codingspace.dto.response.SpaceStatusDto;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Operation(summary = "코딩 스페이스 생성 페이지 조회", description = "코딩 스페이스 생성 페이지를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 생성 페이지 조회에 성공했습니다."
    )
    Api<WritePage> getWritePage(Long userId, Long studyId);

    @Operation(summary = "코딩 스페이스 목록 조회", description = "코딩 스페이스 목록을 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 목록 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
            해당 스터디의 스터디원이 아닙니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<CodingSpacesDto> getCodingSpaces(Long studyId, Long userId, FilterDto dto);

    @Operation(summary = "코딩 스페이스 입장", description = "코딩 스페이스에 입장하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 입장에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
                코딩 스페이스에 참여중이지 않습니다.
                종료된 코딩 스페이스입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<SpaceStatusDto> enterSpace(Long codingSpaceId, Long userId);

    @Operation(summary = "코딩 스페이스 대기방 페이지 조회", description = "코딩 스페이스 대기방을 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 테스트 대기방 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            존재하지 않는 코딩 스페이스입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<WaitingPage> getWaitingPage(Long codingSpaceId, Long userId);

    @Operation(summary = "코딩 스페이스 시작", description = "코딩 스페이스 시작을 하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 스페이스 시작이 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
                이미 시작된 코딩 스페이스입니다.
                코딩 스페이스에 참여중이지 않습니다.
                코딩 스페이스 시작은 방장만 할 수 있습니다.
                코딩 스페이스에 입장하지 않았습니다.
                스터디 시작 최소 인원은 2명입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    NoContent startCodingSpace(Long codingSpaceId, Long userId);


    @Operation(summary = "코딩 스페이스 시작 페이지 조회", description = "코딩 스페이스 시작 페에지를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코딩 테스트 시작 페이지 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            존재하지 않는 코딩 스페이스입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<StartingPage> getStartingPage(Long codingSpaceId, Long userId);

}
