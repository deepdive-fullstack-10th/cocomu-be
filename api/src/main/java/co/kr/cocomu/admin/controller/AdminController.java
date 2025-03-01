package co.kr.cocomu.admin.controller;

import co.kr.cocomu.admin.service.AdminService;
import co.kr.cocomu.admin.controller.code.AdminApiCode;
import co.kr.cocomu.admin.controller.docs.AdminControllerDocs;
import co.kr.cocomu.admin.dto.request.CreateWorkbookRequest;
import co.kr.cocomu.admin.dto.request.CreateLanguageRequest;
import co.kr.cocomu.admin.dto.response.WorkbookResponse;
import co.kr.cocomu.admin.dto.response.LanguageResponse;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController implements AdminControllerDocs {

    private final AdminService adminService;

    @PostMapping("/studies/workbooks")
    public Api<WorkbookResponse> addWorkbook(@Valid @RequestBody final CreateWorkbookRequest dto) {
        final WorkbookResponse result = adminService.addWorkbook(dto);
        return Api.of(AdminApiCode.ADD_WORKBOOK_SUCCESS, result);
    }

    @PostMapping("/studies/languages")
    public Api<LanguageResponse> addLanguage(@Valid @RequestBody final CreateLanguageRequest dto) {
        final LanguageResponse result = adminService.addLanguage(dto);
        return Api.of(AdminApiCode.ADD_LANGUAGE_SUCCESS, result);
    }

    @DeleteMapping("/studies/workbooks/{workbookId}")
    public NoContent deleteWorkbook(@PathVariable final Long workbookId) {
        adminService.deleteWorkbook(workbookId);
        return NoContent.from(AdminApiCode.DELETE_WORKBOOK_SUCCESS);
    }

    @DeleteMapping("/studies/languages/{languageId}")
    public NoContent deleteLanguage(@PathVariable final Long languageId) {
        adminService.deleteLanguage(languageId);
        return NoContent.from(AdminApiCode.DELETE_LANGUAGE_SUCCESS);
    }

}
