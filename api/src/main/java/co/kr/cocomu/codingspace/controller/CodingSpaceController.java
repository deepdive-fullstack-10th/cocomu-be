package co.kr.cocomu.codingspace.controller;

import co.kr.cocomu.codingspace.controller.code.CodingSpaceApiCode;
import co.kr.cocomu.codingspace.controller.docs.CodingSpaceControllerDocs;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceTabIdDto;
import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import co.kr.cocomu.common.api.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coding-spaces")
public class CodingSpaceController implements CodingSpaceControllerDocs {

    private final CodingSpaceCommandService codingSpaceCommandService;

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
        final String tabId = codingSpaceCommandService.joinCodingSpace(codingSpaceId, userId);
        return Api.of(CodingSpaceApiCode.JOIN_CODING_SPACE_SUCCESS, new CodingSpaceTabIdDto(tabId));
    }

}
