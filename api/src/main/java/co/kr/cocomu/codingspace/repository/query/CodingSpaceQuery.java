package co.kr.cocomu.codingspace.repository.query;

import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.response.page.WaitingPage;
import java.util.List;
import java.util.Optional;

public interface CodingSpaceQuery {

    List<CodingSpaceDto> findSpacesWithFilter(Long userId, Long studyId, FilterDto dto);
    Optional<WaitingPage> findWaitingPage(final Long codingSpaceId);

}
