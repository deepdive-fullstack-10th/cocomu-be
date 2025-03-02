package co.kr.cocomu.study.controller.docs;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "003. COCOMU-STUDY", description = "코코무 스터디 관련 API")
public interface StudyControllerDocs {

    @Operation(summary = "공개 스터디 생성", description = "공개 스터디를 생성하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 생성에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            사용자를 찾을 수 없습니다.
        """,
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
        description = """
            사용자를 찾을 수 없습니다.
            존재하지 않는 스터디입니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<Long> joinPublicStudy(Long userId, Long studyId);

    @Operation(summary = "전체 스터디 조회", description = "전체 스터디를 각 필터링을 기준으로 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "전체 스터디 조회에 성공했습니다."
    )
    Api<AllStudyPageDto> getAllStudyPage(Long userId, GetAllStudyFilterDto filter);

    @Operation(summary = "전체 스터디 문제집 조회", description = "전체 스터디의 문제집을 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "전체 스터디 문제집 조회에 성공했습니다."
    )
    Api<List<WorkbookDto>> getAllWorkbooks();

    @Operation(summary = "전체 스터디 언어 조회", description = "전체 스터디의 언어를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "전체 스터디 언어 조회에 성공했습니다."
    )
    Api<List<LanguageDto>> getAllLanguages();

}
