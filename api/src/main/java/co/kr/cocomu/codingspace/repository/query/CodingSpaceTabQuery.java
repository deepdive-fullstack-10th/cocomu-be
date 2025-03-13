package co.kr.cocomu.codingspace.repository.query;

import co.kr.cocomu.codingspace.dto.response.AllTabDto;
import co.kr.cocomu.codingspace.dto.response.FinishTabDto;
import co.kr.cocomu.codingspace.dto.response.UserDto;
import java.util.List;
import java.util.Map;

public interface CodingSpaceTabQuery {

    Map<Long, List<UserDto>> findUsersBySpace(List<Long> spaceIds);

    List<UserDto> findUsers(Long codingSpaceId, Long userId);

    List<AllTabDto> findAllTabs(Long codingSpaceId, Long userId);

    List<FinishTabDto> findAllFinishedTabs(Long codingSpaceId);

    Map<Long, Long> countSpacesByStudyAndUsers(Long studyId, List<Long> userIds);

}
