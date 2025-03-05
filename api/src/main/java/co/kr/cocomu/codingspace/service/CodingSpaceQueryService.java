package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.codingspace.dto.response.UserDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.codingspace.repository.CodingSpaceTabRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.service.StudyDomainService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CodingSpaceQueryService {

    private final StudyDomainService studyDomainService;
    private final CodingSpaceDomainService codingSpaceDomainService;
    private final CodingSpaceRepository codingSpaceQuery;
    private final CodingSpaceTabRepository codingSpaceTabQuery;
    private final CodingSpaceRepository codingSpaceRepository;

    public List<LanguageDto> getStudyLanguages(final Long userId, final Long studyId) {
        final Study study = studyDomainService.getStudyWithThrow(studyId);
        studyDomainService.validateStudyMembership(userId, studyId);
        return study.getLanguagesDto();
    }

    public CodingSpacesDto getCodingSpaces(final Long studyId, final Long userId, final FilterDto dto) {
        studyDomainService.validateStudyMembership(userId, studyId);
        final List<CodingSpaceDto> codingSpaces = codingSpaceQuery.findSpacesWithFilter(userId, studyId, dto);
        final List<Long> codingSpaceIds = codingSpaces.stream().map(CodingSpaceDto::getId).toList();
        final Map<Long, List<UserDto>> usersBySpace = codingSpaceTabQuery.findUsersBySpace(codingSpaceIds);

        return CodingSpacesDto.of(codingSpaces, usersBySpace);
    }

    public CodingSpaceDto getCodingSpace(final Long codingSpaceId, final Long userId) {
        CodingSpace codingSpace = codingSpaceDomainService.getCodingSpaceWithThrow(codingSpaceId);
        codingSpaceDomainService.validateCodingSpaceMemberShip(codingSpaceId, userId);
        return null;
    }

}
