package co.kr.cocomu.admin.controller;

import co.kr.cocomu.admin.service.AdminService;
import co.kr.cocomu.admin.controller.code.AdminApiCode;
import co.kr.cocomu.admin.controller.docs.AdminControllerDocs;
import co.kr.cocomu.admin.dto.request.CreateJudgeRequest;
import co.kr.cocomu.admin.dto.request.CreateLanguageRequest;
import co.kr.cocomu.admin.dto.response.JudgeResponse;
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

    @PostMapping("/studies/judges")
    public Api<JudgeResponse> addJudge(@Valid @RequestBody final CreateJudgeRequest dto) {
        final JudgeResponse result = adminService.addJudge(dto);
        return Api.of(AdminApiCode.ADD_JUDGE_SUCCESS, result);
    }

    @PostMapping("/studies/languages")
    public Api<LanguageResponse> addLanguage(@Valid @RequestBody final CreateLanguageRequest dto) {
        final LanguageResponse result = adminService.addLanguage(dto);
        return Api.of(AdminApiCode.ADD_LANGUAGE_SUCCESS, result);
    }

    @DeleteMapping("/studies/judges/{judgeId}")
    public NoContent deleteJudge(@PathVariable final Long judgeId) {
        adminService.deleteJudge(judgeId);
        return NoContent.from(AdminApiCode.DELETE_JUDGE_SUCCESS);
    }

    @DeleteMapping("/studies/languages/{languageId}")
    public NoContent deleteLanguage(@PathVariable final Long languageId) {
        adminService.deleteLanguage(languageId);
        return NoContent.from(AdminApiCode.DELETE_LANGUAGE_SUCCESS);
    }

}
