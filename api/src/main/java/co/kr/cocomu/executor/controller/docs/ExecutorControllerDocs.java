package co.kr.cocomu.executor.controller.docs;

import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "005. COCOMU-CODE-EXECUTOR", description = "코코무 코드 실행 관련 API")
public interface ExecutorControllerDocs {

    @Operation(summary = "코딩 스페이스 코드 실행", description = "코딩 스페이스에서 작성한 코드를 실행하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코드 실행이 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
            활성화 되지 않은 탭 입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    NoContent executeCode(ExecuteDto dto);

    @Operation(summary = "코딩 스페이스 코드 제출", description = "코딩 스페이스에서 작성한 코드를 제출하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "코드 제출이 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
              활성화 되지 않은 탭 입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = """
                존재하지 않는 코딩 스페이스입니다.
            """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    NoContent submitCode(SubmitDto dto);

}
