package co.kr.cocomu.study.controller;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.study.controller.code.StudyApiCode;
import co.kr.cocomu.study.controller.docs.StudyControllerDocs;
import co.kr.cocomu.study.controller.query.StudyQueryRepository;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.service.StudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final StudyQueryRepository studyQueryRepository;

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
        final Long publicStudyId = studyService.joinPublicStudy(userId, studyId);
        return Api.of(StudyApiCode.JOIN_STUDY_SUCCESS, publicStudyId);
    }

    @GetMapping
    public Api<AllStudyPageDto> getAllStudyPage(
        @AuthenticationPrincipal final Long userId,
        @ModelAttribute final GetAllStudyFilterDto filter
    ) {
        final AllStudyPageDto studyPages = studyQueryRepository.findTop20StudyPagesWithFilter(filter, userId);
        return Api.of(StudyApiCode.GET_ALL_STUDIES_SUCCESS, studyPages);
    }

}
