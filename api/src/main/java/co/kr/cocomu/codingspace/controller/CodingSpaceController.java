package co.kr.cocomu.codingspace.controller;

import co.kr.cocomu.codingspace.controller.code.CodingSpaceApiCode;
import co.kr.cocomu.codingspace.controller.docs.CodingSpaceControllerDocs;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceTabIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.dto.page.WaitingPage;
import co.kr.cocomu.codingspace.dto.page.WritePage;
import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import co.kr.cocomu.codingspace.service.CodingSpaceQueryService;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coding-spaces")
public class CodingSpaceController implements CodingSpaceControllerDocs {

    private final CodingSpaceCommandService codingSpaceCommandService;
    private final CodingSpaceQueryService codingSpaceQueryService;

    @GetMapping("/write")
    public Api<WritePage> getWritePage(
        @AuthenticationPrincipal final Long userId,
        @RequestParam final Long studyId
    ) {
        final List<LanguageDto> languages = codingSpaceQueryService.getStudyLanguages(userId, studyId);
        final WritePage result = new WritePage(languages);
        return Api.of(CodingSpaceApiCode.GET_WRITE_PAGE_SUCCESS, result);
    }

    @PostMapping
    public Api<CodingSpaceIdDto> createCodingSpace(
        @RequestBody @Valid final CreateCodingSpaceDto dto,
        @AuthenticationPrincipal final Long userId
    ) {
        final Long codingSpaceId = codingSpaceCommandService.createCodingSpace(dto, userId);
        return Api.of(CodingSpaceApiCode.CREATE_CODING_SPACE_SUCCESS, new CodingSpaceIdDto(codingSpaceId));
    }

    @PostMapping("/{codingSpaceId}")
    public Api<CodingSpaceTabIdDto> joinCodingSpace(
        @PathVariable final Long codingSpaceId,
        @AuthenticationPrincipal final Long userId
    ) {
        final Long tabId = codingSpaceCommandService.joinCodingSpace(codingSpaceId, userId);
        return Api.of(CodingSpaceApiCode.JOIN_CODING_SPACE_SUCCESS, new CodingSpaceTabIdDto(tabId));
    }

    @GetMapping("/studies/{studyId}")
    public Api<CodingSpacesDto> getCodingSpaces(
        @PathVariable final Long studyId,
        @AuthenticationPrincipal final Long userId,
        @ModelAttribute final FilterDto dto
    ) {
        final CodingSpacesDto result = codingSpaceQueryService.getCodingSpaces(studyId, userId, dto);
        return Api.of(CodingSpaceApiCode.GET_CODING_SPACES_SUCCESS, result);
    }

    @PostMapping("/{codingSpaceId}/waiting")
    public Api<WaitingPage> enterWaitingSpace(
        @PathVariable final Long codingSpaceId,
        @AuthenticationPrincipal final Long userId
    ) {
        codingSpaceCommandService.enterWaitingSpace(codingSpaceId, userId);
        final WaitingPage result = codingSpaceQueryService.extractWaitingPage(codingSpaceId, userId);
        return Api.of(CodingSpaceApiCode.ENTER_WAITING_SPACE_SUCCESS, result);
    }

    @PostMapping("/{codingSpaceId}/start")
    public NoContent startCodingSpace(
        @PathVariable final Long codingSpaceId,
        @AuthenticationPrincipal final Long userId
    ) {
        codingSpaceCommandService.startSpace(codingSpaceId, userId);
        return NoContent.from(CodingSpaceApiCode.START_CODING_SPACE);
    }

}
