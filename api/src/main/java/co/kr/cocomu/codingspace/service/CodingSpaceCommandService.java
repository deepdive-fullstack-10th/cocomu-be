package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.codingspace.stomp.StompSSEProducer;
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
    private final StompSSEProducer stompSSEProducer;

    public Long createCodingSpace(final CreateCodingSpaceDto dto, final Long userId) {
        final Study study = studyDomainService.getStudyWithThrow(dto.studyId());
        final User user = userService.getUserWithThrow(userId);
        studyDomainService.validateStudyMembership(user.getId(), study.getId());

        final CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, study, user);
        final CodingSpace savedCodingSpace = codingSpaceRepository.save(codingSpace);

        return savedCodingSpace.getId();
    }

    public Long joinCodingSpace(final Long codingSpaceId, final Long userId) {
        final CodingSpace codingSpace = codingSpaceDomainService.getCodingSpaceWithThrow(codingSpaceId);
        final User user = userService.getUserWithThrow(userId);
        studyDomainService.validateStudyMembership(user.getId(), codingSpace.getStudy().getId());
        codingSpace.joinUser(user);

        return codingSpace.getId();
    }

    public String enterWaitingSpace(final Long codingSpaceId, final Long userId) {
        final CodingSpaceTab tab = codingSpaceDomainService.getCodingSpaceTabWithThrow(codingSpaceId, userId);
        tab.enterTab();

        final User user = tab.getUser();
        stompSSEProducer.publishUserEnter(user, codingSpaceId);

        return tab.getDocumentKey();
    }

    public void leaveSpace(final Long userId, final Long codingSpaceId) {
        final CodingSpaceTab tab = codingSpaceDomainService.getCodingSpaceTabWithThrow(codingSpaceId, userId);
        tab.leaveTab();

        final User user = tab.getUser();
        stompSSEProducer.publishUserLeave(user, codingSpaceId);
    }

}
