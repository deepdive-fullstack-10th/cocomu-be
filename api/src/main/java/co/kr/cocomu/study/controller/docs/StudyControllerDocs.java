package co.kr.cocomu.study.controller.docs;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "003. COCOMU-STUDY", description = "코코무 스터디 관련 API")
public interface StudyControllerDocs {

    @Operation(summary = "공개 스터디 생성", description = "공개 스터디를 생성하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 생성에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = "사용자를 찾을 수 없습니다.\n\"존재하지 않는 스터디입니다.\n",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<Long> createPublicStudy(Long userId, CreatePublicStudyDto dto);

    @Operation(summary = "공개 스터디 참여", description = "공개 스터디를 참여하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 참여에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = "사용자를 찾을 수 없습니다.\n\"존재하지 않는 스터디입니다.\n",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<Long> joinPublicStudy(Long userId, Long studyId);

}
