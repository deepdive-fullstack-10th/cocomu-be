package co.kr.cocomu.codingspace.service;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.codingspace.repository.CodingSpaceRepository;
import co.kr.cocomu.codingspace.repository.CodingSpaceTabRepository;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodingSpaceDomainService {

    private final CodingSpaceRepository codingSpaceRepository;
    private final CodingSpaceTabRepository codingSpaceTabRepository;

    public CodingSpace getCodingSpaceWithThrow(final Long codingSpaceId) {
        return codingSpaceRepository.findById(codingSpaceId)
            .orElseThrow(() -> new NotFoundException(CodingSpaceExceptionCode.NOT_FOUND_SPACE));
    }

    // 사용하려헀지만 사용되지 않음 나중에 필요없으면 코드 개선하기
    public void validateCodingSpaceMemberShip(final Long codingSpaceId, final Long userId) {
        if (codingSpaceTabRepository.existsByUserIdAndCodingSpaceId(userId, codingSpaceId)) {
            throw new BadRequestException(CodingSpaceExceptionCode.ALREADY_PARTICIPATION_SPACE);
        }
    }

    public CodingSpaceTab getCodingSpaceTabWithThrow(final Long codingSpaceId, final Long userId) {
        return codingSpaceTabRepository.findByUserIdAndCodingSpaceId(userId, codingSpaceId)
            .orElseThrow(() -> new BadRequestException(CodingSpaceExceptionCode.NO_PARTICIPATION_SPACE));
    }

}
