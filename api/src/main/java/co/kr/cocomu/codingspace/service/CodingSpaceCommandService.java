package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.dto.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.service.StudyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CodingSpaceCommandService {

    private final StudyDomainService studyDomainService;
    private final CodingSpaceRepository codingSpaceRepository;

    public Long createCodingSpace(final CreateCodingSpaceDto dto, final Long userId) {
        final Study study = studyDomainService.getStudyWithThrow(dto.studyId());
        studyDomainService.validateStudyMembership(userId, dto.studyId());

        final CodingSpace codingSpace = CodingSpace.createCodingSpace(dto, study);
        final CodingSpace savedCodingSpace = codingSpaceRepository.save(codingSpace);
        return savedCodingSpace.getId();
    }

}
