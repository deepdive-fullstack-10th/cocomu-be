package co.kr.cocomu.admin.controller.docs;

import co.kr.cocomu.admin.dto.request.CreateWorkbookRequest;
import co.kr.cocomu.admin.dto.request.CreateLanguageRequest;
import co.kr.cocomu.admin.dto.response.WorkbookResponse;
import co.kr.cocomu.admin.dto.response.LanguageResponse;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "000. COCOMU-ADMIN", description = "코코무 관리자 관련 API")
public interface AdminControllerDocs {

    @Operation(summary = "문제집 정보 추가", description = "스터디에 문제집 정보를 추가하는 기능")
    @ApiResponse(
        responseCode = "10000",
        description = "스터디 문제집 정보가 추가되었습니다."
    )
    Api<WorkbookResponse> addWorkbook(CreateWorkbookRequest dto);

    @Operation(summary = "언어 정보 추가", description = "스터디에 언어 정보를 추가하는 기능")
    @ApiResponse(
        responseCode = "10001",
        description = "스터디 언어 정보가 추가되었습니다."
    )
    Api<LanguageResponse> addLanguage(CreateLanguageRequest dto);

    @Operation(summary = "문제집 정보 삭제", description = "스터디에 문제집 정보를 삭제하는 기능")
    @ApiResponse(
        responseCode = "10002",
        description = "스터디 문제집 정보가 삭제되었습니다."
    )
    NoContent deleteWorkbook(Long workbookId);

    @Operation(summary = "언어 정보 삭제", description = "스터디에 언어 정보를 삭제하는 기능")
    @ApiResponse(
        responseCode = "10003",
        description = "스터디 언어 정보가 삭제되었습니다."
    )
    NoContent deleteLanguage(Long languageId);

}
