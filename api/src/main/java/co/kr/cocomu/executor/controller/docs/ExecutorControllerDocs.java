package co.kr.cocomu.executor.controller.docs;

import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(hidden = true)
    NoContent handleExecutionResult(EventMessage<ExecutionMessage> dto);

}
