package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.service.StudyDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CodingSpaceQueryService {

    private final StudyDomainService studyDomainService;

    public List<LanguageDto> getStudyLanguages(final Long userId, final Long studyId) {
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        studyDomainService.validateStudyMembership(userId, studyId);
        return study.getLanguagesDto();
    }


}
