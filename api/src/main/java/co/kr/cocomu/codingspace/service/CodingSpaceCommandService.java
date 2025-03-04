package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.service.StudyDomainService;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CodingSpaceCommandService {

    private final CodingSpaceDomainService codingSpaceDomainService;
    private final StudyDomainService studyDomainService;
    private final UserService userService;
    private final CodingSpaceRepository codingSpaceRepository;

    public Long createCodingSpace(final CreateCodingSpaceDto dto, final Long userId) {
        final Study study = studyDomainService.getStudyWithThrow(dto.studyId());
        final User user = userService.getUserWithThrow(userId);
        studyDomainService.validateStudyMembership(user.getId(), study.getId());

        final CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, study, user);
        final CodingSpace savedCodingSpace = codingSpaceRepository.save(codingSpace);

        return savedCodingSpace.getId();
    }

    public String joinCodingSpace(final Long codingSpaceId, final Long userId) {
        final CodingSpace codingSpace = codingSpaceDomainService.getWaitingCodingSpaceWithThrow(codingSpaceId);
        final User user = userService.getUserWithThrow(userId);
        studyDomainService.validateStudyMembership(user.getId(), codingSpace.getStudy().getId());

        final CodingSpaceTab codingSpaceTab = codingSpace.joinUser(user);
        return codingSpaceTab.getId();
    }

}
