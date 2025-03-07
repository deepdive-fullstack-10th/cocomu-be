package co.kr.cocomu.codingspace.repository.query;

import co.kr.cocomu.codingspace.dto.response.AllTabDto;
import co.kr.cocomu.codingspace.dto.response.FinishTabDto;
import co.kr.cocomu.codingspace.dto.response.UserDto;
import java.util.List;
import java.util.Map;

public interface CodingSpaceTabQuery {

    Map<Long, List<UserDto>> findUsersBySpace(List<Long> spaceIds);

    List<UserDto> findUsers(Long codingSpaceId);

    List<AllTabDto> findAllTabs(Long codingSpaceId);

    List<FinishTabDto> findAllFinishedTabs(Long codingSpaceId);

}
