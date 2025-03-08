package co.kr.cocomu.study.controller.docs;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyCardDto;
import co.kr.cocomu.study.dto.response.StudyCardDto;
import co.kr.cocomu.study.dto.page.StudyDetailPageDto;
import co.kr.cocomu.study.dto.page.StudyPageDto;
import co.kr.cocomu.study.dto.response.FilterOptionsDto;
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
    Api<AllStudyCardDto> getAllStudyCard(Long userId, GetAllStudyFilterDto filter);

    @Operation(summary = "스터디 정보 페이지 조회", description = "스터디의 정보 페이지를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 정보 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 스터디입니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<StudyCardDto> getStudyInfo(Long userId, Long studyId);

    @Operation(summary = "스터디 리스트 페이지 조회", description = "스터디 목록 페이지를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 리스트 페이지 조회에 성공했습니다."
    )
    Api<StudyPageDto> getStudiesPage(Long userId);

    @Operation(summary = "스터디 상세 페이지 조회", description = "스터디 상세 페이지를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 상세 페이지 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = "해당 스터디의 스터디원이 아닙니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 스터디입니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<StudyDetailPageDto> getStudyDetailPage(Long studyId, Long userId);

    @Operation(summary = "스터디 필터 옵션 조회", description = "스터디 필터 옵션들을 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "스터디 필터 옵션 조회에 성공했습니다."
    )
    Api<FilterOptionsDto> getFilterOptions(Long userId);
}
