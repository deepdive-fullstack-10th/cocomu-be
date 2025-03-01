package co.kr.cocomu.study.controller;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.study.controller.code.StudyApiCode;
import co.kr.cocomu.study.controller.docs.StudyControllerDocs;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.service.StudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController implements StudyControllerDocs {

    private final StudyService studyService;

    @PostMapping("/public")
    public Api<Long> createPublicStudy(
        @AuthenticationPrincipal final Long userId,
        @Valid @RequestBody final CreatePublicStudyDto dto
    ) {
        final Long publicStudyId = studyService.createPublicStudy(userId, dto);
        return Api.of(StudyApiCode.CREATE_STUDY_SUCCESS, publicStudyId);
    }

    @PostMapping("/public/{studyId}/join")
    public Api<Long> joinPublicStudy(
        @AuthenticationPrincipal final Long userId,
        @PathVariable final Long studyId
    ) {
        final Long publicStudyId = studyService.joinPublicStudy(userId, studyId, StudyRole.NORMAL);
        return Api.of(StudyApiCode.JOIN_STUDY_SUCCESS, publicStudyId);
    }

}
