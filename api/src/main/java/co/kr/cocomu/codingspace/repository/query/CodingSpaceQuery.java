package co.kr.cocomu.codingspace.repository.query;

import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.codingspace.dto.page.WaitingPage;
import java.util.List;

public interface CodingSpaceQuery {

    List<CodingSpaceDto> findSpacesWithFilter(Long userId, Long studyId, FilterDto dto);
    WaitingPage findWaitingPage(Long codingSpaceId, Long userId);

}
